package com.mentos1386.evegator.Views;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mentos1386.evegator.Controllers.AuthController;
import com.mentos1386.evegator.Controllers.EdgeWeightController;
import com.mentos1386.evegator.Controllers.PathController;
import com.mentos1386.evegator.Endpoint;
import com.mentos1386.evegator.EveGator;
import com.mentos1386.evegator.ExceptionHandler;
import com.mentos1386.evegator.Interfaces.ViewInterface;
import com.mentos1386.evegator.Models.*;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.Graph;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.List;

public class Main implements ViewInterface {

    private final int HEIGHT = 800;
    private final int WIDTH = 1000;
    private Graph<SolarSystemObject, StargateObject> graph;

    private RegionObject selectedRegion;
    private ConstellationObject selectedConstellation;
    private SolarSystemObject selectedSolarSystem;
    private ListView<RegionObject> regionsList;
    private ListView<ConstellationObject> constellationsList;
    private ListView<SolarSystemObject> solarSystemsList;
    private ListView<StargateObject> navigationList;
    public static ObservableList<StargateObject> stargates;
    private ObservableList<ConstellationObject> constellations;
    private ObservableList<SolarSystemObject> solarSystems;

    private DijkstraShortestPath<SolarSystemObject, StargateObject> dist;

    private SolarSystemObject locationSolarSystem;
    private String locationStation = "Not in station";

    private GridPane navigation;
    private GridPane navigationControlls;
    private GridPane navigationControllsButtons;
    private Text fromText;
    private Text toText;
    private Text destinationText;
    private Text locationSolarSystemText;
    private Text locationStationText;
    private Button refreshButton;
    private Button calculateButton;
    private Button navigateButton;
    private ChoiceBox<String> securityPenalty;
    private CheckBox avoidence;

    public static String color(double sec) {
        String color;
        if (sec >= 1.0) {
            color = "#2FEFEF";
        } else if (sec >= 0.9) {
            color = "#48F0C0";
        } else if (sec >= 0.8) {
            color = "#00EF47";
        } else if (sec >= 0.7) {
            color = "#00F000";
        } else if (sec >= 0.6) {
            color = "#8FEF2F";
        } else if (sec >= 0.5) {
            color = "#EFEF00";
        } else if (sec >= 0.4) {
            color = "#D77700";
        } else if (sec >= 0.3) {
            color = "#F06000";
        } else if (sec >= 0.2) {
            color = "#F04800";
        } else if (sec >= 0.1) {
            color = "#D73000";
        } else {
            color = "#F00000";
        }
        return color;
    }

    @Override
    public Scene build() {

        BorderPane root = new BorderPane();
        GridPane grid = new GridPane();

        this.getLocation();
        this.graph = new PathController().init();

        ColumnConstraints cc1 = new ColumnConstraints(WIDTH / 4);
        ColumnConstraints cc2 = new ColumnConstraints(WIDTH / 4);
        ColumnConstraints cc3 = new ColumnConstraints(WIDTH / 4);
        ColumnConstraints cc4 = new ColumnConstraints(WIDTH / 4);
        cc4.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(cc1, cc2, cc3, cc4);

        grid.add(regions(), 0, 0);
        grid.add(constellations(), 1, 0);
        grid.add(solarSystems(), 2, 0);

        this.navigation = new GridPane();
        grid.add(navigation, 3, 0);

        this.navigationControlls = new GridPane();
        navigation.add(navigationControlls, 0, 0);

        // Initialize components
        locationInit();
        navigationInit();

        this.navigationControlls.add(fromText, 0, 0);
        this.navigationControlls.add(locationSolarSystemText, 0, 1);
        //navigation.add(locationStationText, 0, 1);
        this.navigationControlls.add(toText, 0, 2);
        this.navigationControlls.add(destinationText, 0, 3);

        this.navigationControllsButtons = new GridPane();
        this.navigationControlls.add(navigationControllsButtons, 0, 4);

        this.navigationControllsButtons.addRow(0, refreshButton, calculateButton, navigateButton);
        this.navigationControllsButtons.addRow(1, securityPenalty, avoidence);

        this.navigation.add(navigationList, 0, 1);

        root.setCenter(grid);
        return new Scene(root, WIDTH, HEIGHT);
    }

    private ListView<RegionObject> regions() {
        ObservableList<RegionObject> regions = FXCollections.observableArrayList();
        // Remove wormhole space
        EveGator.dataCon.regions().forEach((integer, regionObject) -> {
            if (!String.valueOf(regionObject.getId()).startsWith("11")) {
                regions.add(regionObject);
            }
        });
        regions.sort(null);
        this.regionsList = new ListView<>(regions);
        this.regionsList.setPrefSize(WIDTH / 4, HEIGHT);
        this.regionsList.setItems(regions);

        this.regionsList.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                this.selectedRegion = this.regionsList.getSelectionModel().getSelectedItem();

            } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                RegionObject reg = this.regionsList.getSelectionModel().getSelectedItem();

                if (PathController.avoidListRegions.contains(reg)) {
                    PathController.avoidListRegions.remove(reg);
                    System.out.println("Avoid remove region " + reg.getName());
                } else {
                    PathController.avoidListRegions.add(reg);
                    System.out.println("Avoid add region " + reg.getName());
                }
                this.solarSystemsList.refresh();
            }
            this.updateConstellations();
            this.updateSolarSystemsRegion();
        });

        return this.regionsList;
    }

    private void navigationInit() {
        this.calculateButton = new Button("Calculate");
        this.calculateButton.setOnAction(e -> {
            try {
                if (this.locationSolarSystem == null) {
                    throw new Exception("You have to be in game for location to work!");
                } else if (this.selectedSolarSystem == null) {
                    throw new Exception("Please select destination first!");
                } else {
                    this.dist = new DijkstraShortestPath<>(this.graph, new EdgeWeightController(), false);
                    List<StargateObject> d = dist.getPath(this.locationSolarSystem, this.selectedSolarSystem);
                    System.out.println(d);
                    this.stargates.clear();
                    this.stargates.addAll(d);
                }
            } catch (Exception ex) {
                new ExceptionHandler(ex);
            }
        });

        this.securityPenalty = new ChoiceBox<>();
        this.securityPenalty.setItems(FXCollections.observableArrayList(
                "Shorter", "Less Secure", "More Secure")
        );
        this.securityPenalty.setValue("More Secure");
        this.securityPenalty.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            PathController.modifier = newValue.intValue();
        });

        this.avoidence = new CheckBox();
        this.avoidence.setText("Avoid enabled");
        this.avoidence.setSelected(true);
        this.avoidence.selectedProperty().addListener((ov, old_val, new_val) -> {
            PathController.avoid = new_val;
        });

        this.navigateButton = new Button();
        this.navigateButton.setText("Navigate");
        this.navigateButton.setOnAction(e -> {
            Thread sendToGame = new Thread() {
                @Override
                public void run() {
                    AuthObject AUTH = AuthController.getAuth();

                    String uri = "characters/" + AUTH.OWNER + "/navigation/waypoints/";

                    int cnt = 0;
                    for (StargateObject sg: Main.stargates) {

                        int id = sg.getToSolarSystem().getId();
                        String href = "https://crest-tq.eveonline.com/solarsystems/" + id + "/";

                        boolean first = false;
                        boolean clear = false;

                        if(cnt == 0)
                        {
                            first = true;
                            clear = true;
                        }

                        String data  = getWaypointString(id, href, first, clear);

                        new Endpoint(uri).post(data);

                        System.out.println(cnt +" " + first +" " + clear + " " + data);
                        cnt ++;
                    }
                }

                private String getWaypointString(int id, String href, boolean first, boolean clear)
                {
                    return "{" +
                            "  \"clearOtherWaypoints\": "+ clear +"," +
                            "  \"first\": "+ first +"," +
                            "  \"solarSystem\": {" +
                            "    \"href\": \""+ href +"\"," +
                            "    \"id\": "+ id +"" +
                            "  }" +
                            "}";
                }
            };

            sendToGame.start();
        });


        stargates = FXCollections.observableArrayList();
        this.navigationList = new ListView<>(stargates);
        this.navigationList.setCellFactory(list -> new SecurityStatusStargates());
    }

    private void locationInit() {
        // Try to get location, it's "null" if user not logged in
        String locationSolarsystem;
        if (this.locationSolarSystem == null) {
            locationSolarsystem = "Not available";
        } else {
            locationSolarsystem = this.locationSolarSystem.getName();
        }

        this.locationSolarSystemText = new Text(locationSolarsystem);
        locationSolarSystemText.setTextAlignment(TextAlignment.CENTER);
        locationSolarSystemText.setFont(Font.font(40));
        if (this.locationSolarSystem != null) {
            locationSolarSystemText.setFill(Paint.valueOf(color(this.locationSolarSystem.getSecurity())));
        }

        this.locationStationText = new Text(this.locationStation);
        locationStationText.setTextAlignment(TextAlignment.CENTER);
        locationStationText.setFont(Font.font(15));


        this.refreshButton = new Button("refresh");
        refreshButton.setOnAction(e -> {
            this.getLocation();
            if (this.locationSolarSystem != null) {
                locationSolarSystemText.setText(this.locationSolarSystem.getName());
                locationStationText.setText(this.locationStation);
                locationSolarSystemText.setFill(Paint.valueOf(color(this.locationSolarSystem.getSecurity())));
            }
        });

        this.fromText = new Text("FROM");
        fromText.setFont(Font.font(30));

        this.toText = new Text("TO");
        toText.setFont(Font.font(30));

        destinationText = new Text("Not selected");
        destinationText.setFont(Font.font(40));
    }

    private void getLocation() {
        AuthObject AUTH = AuthController.getAuth();

        // Get crest data
        JsonObject locationData = new Endpoint("characters/" + AUTH.OWNER + "/location/").get();

        if (locationData.has("solarSystem")) {
            int id = locationData.get("solarSystem").getAsJsonObject().get("id").getAsInt();
            this.locationSolarSystem = EveGator.dataCon.solarSystems().get(id);
        }

        if (locationData.has("station")) {
            this.locationStation = locationData.get("station").getAsJsonObject().get("name").getAsString();
        } else {
            this.locationStation = "Not in station";
        }
    }

    private ListView<ConstellationObject> constellations() {

        this.constellations = FXCollections.observableArrayList();

        this.updateConstellations();

        this.constellationsList = new ListView<>(this.constellations);
        this.constellationsList.setPrefSize(WIDTH / 4, HEIGHT);
        this.constellationsList.setItems(this.constellations);

        this.constellationsList.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                this.selectedConstellation = this.constellationsList.getSelectionModel().getSelectedItem();

            } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                ConstellationObject con = this.constellationsList.getSelectionModel().getSelectedItem();

                if (PathController.avoidListConstellations.contains(con)) {
                    PathController.avoidListConstellations.remove(con);
                    System.out.println("Avoid remove constellation " + con.getName());
                } else {
                    PathController.avoidListConstellations.add(con);
                    System.out.println("Avoid add constellation " + con.getName());
                }
                this.solarSystemsList.refresh();
            }
            this.updateSolarSystemsConstellation();
        });

        return this.constellationsList;
    }

    private void updateConstellations() {
        if (this.selectedRegion != null) {
            this.constellations.clear();
            EveGator.dataCon.constellations().values().forEach(constellationObject -> {
                if (constellationObject.getRegionId() == this.selectedRegion.getId()) {
                    this.constellations.add(constellationObject);
                }
            });
            this.constellations.sort(null);
        }
    }

    private ListView<SolarSystemObject> solarSystems() {

        this.solarSystems = FXCollections.observableArrayList();

        this.updateSolarSystemsRegion();

        this.solarSystemsList = new ListView<>(this.solarSystems);
        this.solarSystemsList.setPrefSize(WIDTH / 4, HEIGHT);
        this.solarSystemsList.setItems(this.solarSystems);

        this.solarSystemsList.setOnMouseClicked(event -> {
            if (!this.solarSystemsList.getSelectionModel().isEmpty()) {
                SolarSystemObject ss = this.solarSystemsList.getSelectionModel().getSelectedItem();
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    this.selectedSolarSystem = ss;
                    this.destinationText.setText(this.selectedSolarSystem.getName());
                    this.destinationText.setFill(Paint.valueOf(color(this.selectedSolarSystem.getSecurity())));

                } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                    if (PathController.avoidListSolarSystems.contains(ss)) {
                        PathController.avoidListSolarSystems.remove(ss);
                        System.out.println("Avoid remove solarSystem " + ss.getName());
                    } else {
                        PathController.avoidListSolarSystems.add(ss);
                        System.out.println("Avoid add solarSystem " + ss.getName());
                    }
                    this.solarSystemsList.refresh();
                }
            }
        });

        this.solarSystemsList.setCellFactory(list -> new SecurityStatus());


        return this.solarSystemsList;
    }

    private void updateSolarSystemsConstellation() {
        if (this.selectedConstellation != null) {
            this.solarSystems.clear();
            EveGator.dataCon.solarSystems().values().forEach(solarSystemObject -> {
                if (solarSystemObject.getConstellation().getId() == this.selectedConstellation.getId()) {
                    this.solarSystems.add(solarSystemObject);
                }
            });
            this.solarSystems.sort(null);
        }
    }

    private void updateSolarSystemsRegion() {
        if (this.selectedRegion != null) {
            this.solarSystems.clear();
            EveGator.dataCon.solarSystems().values().forEach(solarSystemObject -> {
                if (solarSystemObject.getRegion().getId() == this.selectedRegion.getId()) {
                    this.solarSystems.add(solarSystemObject);
                }
            });
            this.solarSystems.sort(null);
        }
    }

    static class SecurityStatus extends ListCell<SolarSystemObject> {
        @Override
        public void updateItem(SolarSystemObject item, boolean empty) {
            super.updateItem(item, empty);

            Paint fill = Color.LIGHTGRAY;
            Background background = new Background(new BackgroundFill(fill, null, null));

            Rectangle rect = new Rectangle(15, 15);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                rect.setFill(Color.web(Main.color(item.getSecurity())));
                setGraphic(rect);
                setText(item.getName());
                if (PathController.avoidListSolarSystems.contains(item) ||
                        PathController.avoidListConstellations.contains(item.getConstellation()) ||
                        PathController.avoidListRegions.contains(item.getRegion())
                        ) {
                    fill = Paint.valueOf("#fdd835");
                    background = new Background(new BackgroundFill(fill, null, null));
                }
            }

            setBackground(background);
        }
    }

    static class SecurityStatusStargates extends ListCell<StargateObject> {
        @Override
        public void updateItem(StargateObject item, boolean empty) {
            super.updateItem(item, empty);
            Rectangle rect = new Rectangle(15, 15);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                rect.setFill(Color.web(Main.color(item.getToSolarSystem().getSecurity())));
                setGraphic(rect);
                setText(item.getToSolarSystem().getName());
            }
        }
    }
}
