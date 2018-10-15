package me.theeninja.islandroyale.ai;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.entity.*;
import me.theeninja.islandroyale.entity.controllable.ControllableEntity;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.api.Updater;
import org.deeplearning4j.nn.conf.*;
import org.deeplearning4j.nn.conf.ComputationGraphConfiguration.GraphBuilder;
import org.deeplearning4j.nn.conf.graph.MergeVertex;
import org.deeplearning4j.nn.conf.graph.rnn.DuplicateToTimeSeriesVertex;
import org.deeplearning4j.nn.conf.graph.rnn.LastTimeStepVertex;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.collection.IntArrayKeyMap;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.learning.config.IUpdater;
import org.nd4j.linalg.learning.config.RmsProp;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AIPlayer extends Player {
    private int level;
    private final Array<Vector2[]> previousChosenRoutes = new Array<>();
    private final Random random = new Random();

    private static final double BASE_LEARNING_RATE = 0.001;

    private static double getLearningRate(int level) {
        return level * BASE_LEARNING_RATE;
    }
    private final ComputationGraph computationGraph;

    AIPlayer(int level, String playerName, IntMap<Entity<?, ?>> entityMap) {
        super(playerName);
        this.level = level;
        this.computationGraph = this.createComputationGraph(entityMap);

        this.indArrayMap = initializeIndArrayMap(entityMap);
    }

    private Map<INDArray, InteractableEntity<?, ?>> initializeIndArrayMap(final IntMap<Entity<?, ?>> entityMap) {
        final Map<INDArray, InteractableEntity<?, ?>> indArrayMap = new HashMap<>();

        int entityIndex = 0;

        for (final Entity<?, ?> entity : entityMap.values()) {
            if (!(entity.getEntityType() instanceof InteractableEntityType)) {
                continue;
            }

            final InteractableEntity<?, ?> interactableEntity = (InteractableEntity<?, ?>) entity;

            final INDArray indArray = Nd4j.zeros(entityMap.size);
            indArray.putScalar(entityIndex++, 1);

            indArrayMap.put(indArray, interactableEntity);
        }

        return indArrayMap;
    }

    public int getLevel() {
        return level;
    }

    private static final int HIDDEN_LAYER_WIDTH = 256;
    private static final int EMBEDDING_WIDTH = 256;
    private static final double LEARNING_RATE = 0.001;

    private final Map<INDArray, InteractableEntity<?, ?>> indArrayMap;

    private ComputationGraph createComputationGraph(IntMap<Entity<?, ?>> entityMap) {
        final IUpdater adamOptimizer = new Adam(LEARNING_RATE);

        final NeuralNetConfiguration.Builder builder = new NeuralNetConfiguration.Builder()
                .updater(adamOptimizer)
                .weightInit(WeightInit.XAVIER)
                .gradientNormalization(GradientNormalization.RenormalizeL2PerLayer);

        final GraphBuilder graphBuilder = builder.graphBuilder()
                .pretrain(false)
                .backprop(true)
                .backpropType(BackpropType.Standard)
                .addInputs("inputLine", "decoderInput")
                .setInputTypes(InputType.recurrent(entityMap.size), InputType.recurrent(entityMap.size))
                .addLayer("embeddingEncoder",
                        new EmbeddingLayer.Builder()
                                .nIn(entityMap.size)
                                .nOut(EMBEDDING_WIDTH)
                                .build(),
                        "inputLine")
                .addLayer("encoder",
                        new LSTM.Builder()
                                .nIn(EMBEDDING_WIDTH)
                                .nOut(HIDDEN_LAYER_WIDTH)
                                .activation(Activation.TANH)
                                .build(),
                        "embeddingEncoder")
                .addVertex("thoughtVector", new LastTimeStepVertex("inputLine"), "encoder")
                .addVertex("dup", new DuplicateToTimeSeriesVertex("decoderInput"), "thoughtVector")
                .addVertex("merge", new MergeVertex(), "decoderInput", "dup")
                .addLayer("decoder",
                        new LSTM.Builder()
                                .nIn(entityMap.size + HIDDEN_LAYER_WIDTH)
                                .nOut(HIDDEN_LAYER_WIDTH)
                                .activation(Activation.TANH)
                                .build(),
                        "merge")
                .addLayer("output",
                        new RnnOutputLayer.Builder()
                                .nIn(HIDDEN_LAYER_WIDTH)
                                .nOut(entityMap.size)
                                .activation(Activation.SOFTMAX)
                                .lossFunction(LossFunctions.LossFunction.SQUARED_LOSS)
                                .build(),
                        "decoder")
                .setOutputs("output");

        final ComputationGraph net = new ComputationGraph(graphBuilder.build());
        net.init();

        return net;
    }

    private final Array<InteractableEntity<?, ?>> queriedEntities = new Array<>();

    private void updateQueriedEntities() {

    }

    @Override
    public void requestTransportationRoute(ControllableEntity<?, ?> entity, MatchMap matchMap) {
        int pathLength = getRandom().nextInt();

        Vector2[] transportationRoute = new Vector2[pathLength];

        for (int pathComponentIndex = 0; pathComponentIndex < transportationRoute.length; pathComponentIndex++) {
            float pathComponentX = getRandom().nextFloat();
            float pathComponentY = getRandom().nextFloat();

            Vector2 pathComponent = new Vector2(pathComponentX, pathComponentY);

            transportationRoute[pathComponentIndex] = pathComponent;
        }

        entity.getPath().addAll(transportationRoute);
        getPreviousChosenRoutes().add(transportationRoute);
    }

    @Override
    public void update(MatchMap matchMap) {
        InteractableEntity<?, ?> entityResponse = predictEntityResponse(matchMap);

        // No response to current match map, continue
        if (entityResponse == null) {
            return;
        }

        boolean canAfford = entityResponse.getEntityType().getInventoryCost().has(getInventory());

        if (canAfford) {
            matchMap.addEntity(entityResponse);
        }
    }

    private InteractableEntity<?, ?> predictEntityResponse(MatchMap matchMap) {
        return null;
    }

    public Array<Vector2[]> getPreviousChosenRoutes() {
        return previousChosenRoutes;
    }

    public Random getRandom() {
        return random;
    }

    public ComputationGraph getComputationGraph() {
        return computationGraph;
    }

    public Array<InteractableEntity<?, ?>> getQueriedEntities() {
        return queriedEntities;
    }

    public Map<INDArray, InteractableEntity<?, ?>> getIndArrayMap() {
        return indArrayMap;
    }
}
