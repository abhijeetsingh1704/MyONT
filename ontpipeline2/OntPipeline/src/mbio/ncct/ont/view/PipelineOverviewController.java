package mbio.ncct.ont.view;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.IndexedCheckModel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mbio.ncct.ont.MainApp;
import mbio.ncct.ont.model.Pipeline;
import mbio.ncct.ont.util.PipelineUtil;

/**
 * This is the controller of the pipeline overview settings.
 *
 * @author Yan Zhou
 * created on 2019/05/14
 */
public class PipelineOverviewController {
  
  /** Initializes log4j2. */
  private static Logger logger = LogManager.getLogger(PipelineOverviewController.class);
  
  /** Initializes MainApp. */
  public MainApp mainApp;

  /** Initializes PipelineUtil. */
  private PipelineUtil pUtil = new PipelineUtil();
  
  /** Initializes Pipeline. */
  private Pipeline p = new Pipeline();
  
  /** Initializes check box for base calling. */
  @FXML
  private CheckBox cBasecalling;
  
  /** Initializes check box for demultiplexing. */
  @FXML
  private CheckBox cDemultiplexing;
  
  /** Initializes check box for reads filter. */
  @FXML
  private CheckBox cReadsFilter;
 
  /** Initializes check box for assembly. */
  @FXML
  private CheckBox cAssembly;
  
  /** Initializes check box for polishing. */
  @FXML
  private CheckBox cPolishing;
  
  /** Initializes choice box for flowcell ID. */
  @FXML
  private ChoiceBox<String> cbFlowcellId = new ChoiceBox<String>() ;
  
  /** Initializes check box for kit number. */
  @FXML
  private ChoiceBox<String> cbKitNumber = new ChoiceBox<String>() ;
  
  /** Initializes check box for assembly mode. */
  @FXML
  private ChoiceBox<String> cbMode = new ChoiceBox<String>() ;
  
  /** Initializes check box for assembly method. */
  @FXML
  private ChoiceBox<String> cbMethod = new ChoiceBox<String>() ;
  
  /** Initializes text field for Nanopore reads workspace. */
  @FXML
  private TextField tfNanoporeWorkspace;
  
  /** Initializes text field for Illumina reads workspace. */
  @FXML
  private TextField tfIlluminaWorkspace;
  
  /** Initializes text field for threads. */
  @FXML
  private TextField tfThreads;
  
  /** Initializes text field for selected barcodes. */
  @FXML
  private TextField tfSelectedBarcode;
  
  /** Initializes text field for output path. */
  @FXML
  private TextField tfOutputPath;
  
  /** Initializes text field for prefix. */
  @FXML
  private TextField tfPrefix;
  
  /** Initializes text field for sample sheet. */
  @FXML
  private TextField tfSampleSheet;
  
  /** Initializes check combo box for barcode kits. */
  @FXML
  private CheckComboBox<String> ccbBarcodeKits = new CheckComboBox<String>();
  
  /** Initializes text area for qstat result. */
  @FXML
  private TextArea taQstat;
  
  /** Initializes base calling group.  */
  @FXML
  private Group gpBaseCalling;
  
  /** Initializes demultiplexing group.  */
  @FXML
  private Group gpDemultiplexing;
  
  /** Initializes reads filter group.  */
  @FXML
  private Group gpReadsFilter;
  
  /** Initializes assembly group.  */
  @FXML
  private Group gpAssembly;
  
  /** Initializes polishing group.  */
  @FXML
  private Group gpPolishing;
  
  /**
   * Initializes the controller of pipeline overview.
   */
  @FXML
  private void initialize()  { 
    
    ObservableList<String> olFlowcellIds = FXCollections.observableArrayList(pUtil.getFlowcellIds());
    cbFlowcellId.setItems(olFlowcellIds);
    if(olFlowcellIds.contains("FLO-MIN106")) {
      cbFlowcellId.getSelectionModel().select("FLO-PRO001");
    } else {
      cbFlowcellId.getSelectionModel().selectFirst();
      p.setFlowcellId(cbFlowcellId.getSelectionModel().getSelectedItem());
    }
    
    ObservableList<String> olKitNumbers = FXCollections.observableArrayList(pUtil.getKitNumbers());
    cbKitNumber.setItems(olKitNumbers);
    if(olKitNumbers.contains("SQK-LSK109")) {
      cbKitNumber.getSelectionModel().select("SQK-LSK109");
    } else {
      cbKitNumber.getSelectionModel().selectFirst();
      p.setKitNumber(cbKitNumber.getSelectionModel().getSelectedItem());
    }
    
    ObservableList<String> olBarcodeKits = FXCollections.observableArrayList(pUtil.getBarcodeKits());
    ccbBarcodeKits.getItems().addAll(olBarcodeKits);
    IndexedCheckModel<String> icm = ccbBarcodeKits.getCheckModel();
    String[] strArrBarcodeKits = p.getBarcodeKits().replaceAll("\"", "").split(" ");
    for(int i=0;i<strArrBarcodeKits.length;i++) {
      icm.check(strArrBarcodeKits[i]);
    }
    
    ArrayList<String> alMode = new ArrayList<String>();
    alMode.add("conservative");
    alMode.add("normal");
    alMode.add("bold");
    ObservableList<String> olMode = FXCollections.observableArrayList(alMode);
    cbMode.setItems(olMode);
    
    ArrayList<String> alMethod = new ArrayList<String>();
    alMethod.add("Long-read-only assembly");
    alMethod.add("Hybrid assembly");
    ObservableList<String> olMethod = FXCollections.observableArrayList(alMethod);
    cbMethod.setItems(olMethod);
    
    cBasecalling.setSelected(true);
    cDemultiplexing.setSelected(true);
    cReadsFilter.setSelected(true);
    cAssembly.setSelected(true);
    cPolishing.setSelected(true);
    
    tfThreads.setText(p.getThreads());
    tfNanoporeWorkspace.setPromptText("/path/to/your/Nanopore/reads/directory");
    tfIlluminaWorkspace.setPromptText("/path/to/your/Illumina/reads/directory");
    tfOutputPath.setPromptText("/path/to/your/output/directory");
    tfSampleSheet.setPromptText("/path/to/your/sample/sheet/file");
    tfSelectedBarcode.setPromptText("e.g.1,2,3, leave blank for all barcodes.");
    tfPrefix.setPromptText("e.g.ID");
    
    cBasecalling.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if(cBasecalling.isSelected()) {
        gpBaseCalling.setDisable(false);
        p.setIfBasecalling(true);
      } else {
        gpBaseCalling.setDisable(true);
        p.setIfBasecalling(false);
      }
    });
    
    cDemultiplexing.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (cDemultiplexing.isSelected()) {
        gpDemultiplexing.setDisable(false);
        p.setIfDemultiplexing(true);
      } else {
        gpDemultiplexing.setDisable(true);
        p.setIfDemultiplexing(false);
      }
    });
    
    cReadsFilter.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if(cReadsFilter.isSelected()) {
        gpReadsFilter.setDisable(false);
        p.setIfReadsFilter(true);
      } else {
        gpReadsFilter.setDisable(true);
        p.setIfReadsFilter(false);
      }
    });
    
    cAssembly.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if(cAssembly.isSelected()) {
        gpAssembly.setDisable(false);
        p.setIfAssembly(true);
      } else {
        gpAssembly.setDisable(true);
        p.setIfAssembly(false);
      }
    });
    
    cPolishing.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if(cPolishing.isSelected()) {
        gpPolishing.setDisable(false);
        p.setIfPolishing(true);
      } else {
        gpPolishing.setDisable(true);
        p.setIfPolishing(false);
      }
    });
    
    cbFlowcellId.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
      p.setFlowcellId(newValue);
    });
    
    cbKitNumber.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
      p.setKitNumber(newValue);
    });
    
    cbMode.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
      p.setMode(newValue);
    });
    
    cbMethod.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
      p.setMethod(newValue);
    });
    
    tfNanoporeWorkspace.textProperty().addListener((observable, oldValue, newValue) -> {
      p.setOntReadsWorkspace(newValue);
    });
    
    tfIlluminaWorkspace.textProperty().addListener((observable, oldValue, newValue) -> {
      p.setIlluminaReadsWorkspace(newValue);
    });
    
    tfOutputPath.textProperty().addListener((observable, oldValue, newValue) -> {
      p.setOutputPath(newValue);
    });
    
    tfThreads.textProperty().addListener((observable, oldValue, newValue) -> {
      p.setThreads(newValue);
    });

    tfSelectedBarcode.textProperty().addListener((observable, oldValue, newValue) -> {
      p.setSelectedBarcode(newValue);
    });
    
    tfPrefix.textProperty().addListener((observable, oldValue, newValue) -> {
      p.setPrefix(newValue);
    });
    
    ccbBarcodeKits.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
      public void onChanged(ListChangeListener.Change<? extends String> c) {
        p.setBarcodeKits(pUtil.formatBarcodeKits(ccbBarcodeKits.getCheckModel().getCheckedItems().toString()));
      }
    });
    getQstat();
  }

  /**
   * Is called by the main application to give a reference back to itself.
   * @param mainApp Main app.
   */
  public void setMainApp(MainApp mainApp) {
    this.mainApp = mainApp;
  }
   
  /**
   * Called when advanced basecalling button is clicked.
   */
  @FXML
  private void handleAdvancedBasecalling() {
    try {
      // Load the fxml file and create a new stage for the popup dialog.
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainApp.class.getResource("view/AdvancedBasecallingView.fxml"));
      AnchorPane page = (AnchorPane) loader.load();

      // Create the dialog Stage.
      Stage dialogStage = new Stage();
      dialogStage.setTitle("Advanced Basecalling Setting");
      dialogStage.initModality(Modality.WINDOW_MODAL);
      dialogStage.initOwner(mainApp.primaryStage);
      Scene scene = new Scene(page);
      dialogStage.setScene(scene);

      // Set the controller.
      AdvancedBasecallingController controller = loader.getController();
      controller.setDialogStage(dialogStage);
      controller.setGuppyMode(p.getGuppyMode());
      controller.setDevice(p.getDevice());
     
      // Show the dialog and wait until the user closes it.
      dialogStage.showAndWait();
      if(controller.isOK == 1) {
        p.setGuppyMode(controller.cbGuppyMode.getValue());
        p.setDevice(controller.cbDevice.getValue());
      }
    } catch (Exception e) {
      logger.error("Can not load advanced base calling view. " + e);
    }
  }
  
  /**
   * Called when advanced reads filter button is clicked.
   */
  @FXML
  private void handleAdvancedReadsFilter() {
    try {
      // Load the fxml file and create a new stage for the popup dialog.
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainApp.class.getResource("view/AdvancedReadsFilterView.fxml"));
      AnchorPane page = (AnchorPane) loader.load();

      // Create the dialog Stage.
      Stage dialogStage = new Stage();
      dialogStage.setTitle("Advanced Reads Filter Setting");
      dialogStage.initModality(Modality.WINDOW_MODAL);
      dialogStage.initOwner(mainApp.primaryStage);
      Scene scene = new Scene(page);
      dialogStage.setScene(scene);

      // Set the controller.
      AdvancedReadsFilterController controller = loader.getController();
      controller.setDialogStage(dialogStage);
      controller.setReadScore(p.getReadScore());
      controller.setReadLength(p.getReadLength());
      controller.setHeadCrop(p.getHeadCrop());
      controller.setIfAdapterTrimming(p.getIfAdapterTrimming());
      controller.setIfSplitting(p.getIfNoSplit());
   
      // Show the dialog and wait until the user closes it.
      dialogStage.showAndWait();
      if (controller.isOK == 1) {
        p.setReadScore(controller.tfReadScore.getText());
        p.setReadLength(controller.tfReadLength.getText());
        p.setHeadCrop(controller.tfHeadCrop.getText());
        p.setIfAdapterTrimming(controller.cAdapterTrimming.isSelected());
        p.setIfNoSplit(controller.cSplitting.isSelected()); 
      }
    } catch (Exception e) {
      logger.error("Can not load advanced reads filter view. " + e);
    }
  }
  
  /**
   * Called when advanced assembly button is clicked.
   */
  @FXML
  private void handleAdvancedAssembly() {
    try {
      // Load the fxml file and create a new stage for the popup dialog.
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainApp.class.getResource("view/AdvancedAssemblyView.fxml"));
      AnchorPane page = (AnchorPane) loader.load();

      // Create the dialog Stage.
      Stage dialogStage = new Stage();
      dialogStage.setTitle("Advanced Assembly Setting");
      dialogStage.initModality(Modality.WINDOW_MODAL);
      dialogStage.initOwner(mainApp.primaryStage);
      Scene scene = new Scene(page);
      dialogStage.setScene(scene);

      // Set the controller.
      AdvancedAssemblyController controller = loader.getController();
      controller.setDialogStage(dialogStage);
      controller.setIfVcf(p.getIfVcf());
     
      // Show the dialog and wait until the user closes it.
      dialogStage.showAndWait();
      if (controller.isOK == 1) {
        p.setIfVcf(controller.cVcf.isSelected()); 
      }
    } catch (Exception e) {
      logger.error("Can not load advanced assembly view. " + e);
    }
  }
  
  /**
   * Called when advanced polishing button is clicked.
   */
  @FXML
  private void handleAdvancedPolishung() {
    try {
      // Load the fxml file and create a new stage for the popup dialog.
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainApp.class.getResource("view/AdvancedPolishingView.fxml"));
      AnchorPane page = (AnchorPane) loader.load();

      // Create the dialog Stage.
      Stage dialogStage = new Stage();
      dialogStage.setTitle("Advanced Polishing Setting");
      dialogStage.initModality(Modality.WINDOW_MODAL);
      dialogStage.initOwner(mainApp.primaryStage);
      Scene scene = new Scene(page);
      dialogStage.setScene(scene);

      // Set the controller.
      AdvancedPolishingController controller = loader.getController();
      controller.setDialogStage(dialogStage);
      controller.setPtimes(p.getPtimes());
      controller.setBuscoData(p.getBuscoData());
      controller.setIfBusco(p.getIfBusco());
     
      // Show the dialog and wait until the user closes it
      dialogStage.showAndWait();
      if (controller.isOK == 1) {
        p.setPtimes(controller.cbPtimes.getValue()); 
        p.setBuscoData(controller.cbBuscoData.getValue());
        p.setIfBusco(controller.cBusco.isSelected());
      }
    } catch (Exception e) {
      logger.error("Can not load advanced polishing view. " + e);
    }
  }
  
  /**
   * Called when start pipeline button is clicked.
   * Errors check:
   * 01. ONT reads directory is empty.
   * 02. Output directory is empty.
   * 03. The threads number is not a positive integer.
   * 04. No module is selected.
   * 05. The format of selected barcodes is wrong (the right format: 1,2,3,4)
   * 06. User selects "Base calling" but the ONT directory contains no FAST5 file.
   * 07. User selects "Hybrid assembly" but the Illumina reads directory is empty.
   * 08. User selects "Polishing" but the Illumina reads directory is empty.
   * #09. User uploads FAST5 files but do not select "Base calling".
   * 10. User starts the pipeline from "Assembly(hybrid)"/"Polishing", but the prefixes of ONT reads (FASTQ) do not match the prefixes of Illumina reads.
   * 11. Guppy_basecaller is in fast mode, the Flowcell ID does not match the device. (PromethION should match FLO-PRO* and MinION* should match FLO-MIN*.)
   */
  @FXML
  private void handleStartPipeline()  {
    
    if (p.getOntReadsWorkspace().isEmpty()) {
      pUtil.createAlertDialog(AlertType.ERROR, "Empty Nanopore reads directory.", "Nanopore reads directory can not be empty.");
    } else if (p.getOutputPath().isEmpty()) {
      pUtil.createAlertDialog(AlertType.ERROR, "Empty output directory.", "Output directory can not be empty.");
    } else if (!p.getThreads().matches(("\\d+"))){
      pUtil.createAlertDialog(AlertType.ERROR, "Wrong threads.", "Threads should be a positive integer.");
    } else if (!p.getIfBasecalling() && !p.getIfReadsFilter() && !p.getIfAssembly() && !p.getIfPolishing()) {
      pUtil.createAlertDialog(AlertType.ERROR, "Empty module.", "Please select at least one module.");
    } else if (!p.getSelectedBarcode().matches("([123456789],{0,1})*")){
      pUtil.createAlertDialog(AlertType.ERROR, "Wrong seleted barcodes.", "The format of selected barcodes is wrong.");
    } else if (p.getIfBasecalling() && !pUtil.checkDirectoryValidity(new File(p.getOntReadsWorkspace()), "fast5")) {
      pUtil.createAlertDialog(AlertType.ERROR, "Wrong input files.", "Base calling runs only with FAST5 files");
    } else if (p.getIfAssembly() && p.getMethod().equals("Hybrid assembly") && p.getIlluminaReadsWorkspace().isEmpty()) {
      pUtil.createAlertDialog(AlertType.ERROR, "No Illumina reads found.", "Hybrid assembly requires Illumina reads.");
    } else if (p.getIfPolishing() && p.getIlluminaReadsWorkspace().isEmpty()) {
      pUtil.createAlertDialog(AlertType.ERROR, "No Illumina reads found.", "Polishing requires Illumina reads.");
    } else if (pUtil.checkDirectoryValidity(new File(p.getOntReadsWorkspace()), "fast5") && !p.getIfBasecalling()) {
      pUtil.createAlertDialog(AlertType.ERROR, "Base calling required.", "Base calling is required since you provide FAST5 files.");
    } else if (((p.getIfAssembly() && p.getMethod().equals("Hybrid assembly")) || p.getIfPolishing()) && pUtil.checkDirectoryValidity(new File(p.getOntReadsWorkspace()), "fastq")
        && !pUtil.checkOntReadsPrefix(new File(p.getOntReadsWorkspace()), new File(p.getIlluminaReadsWorkspace()))) {
      pUtil.createAlertDialog(AlertType.ERROR, "Wrong prefixes.", "The prefixes of ONT reads do not match the prefixes of Illumina reads.");
    } else if ((p.getIfGuppyFast() && p.getDevice().equals("PromethION") && p.getFlowcellId().startsWith("FLO-MIN") )|| 
        (p.getIfGuppyFast() && !p.getDevice().equals("PromethION") && p.getFlowcellId().startsWith("FLO-PRO"))) {
      pUtil.createAlertDialog(AlertType.ERROR, "Wrong Flowcell ID or device.", "The flowcell ID does not match the device.");
    } else {
      String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
      pUtil.createUserLog(p, timestamp);
      pUtil.createPbsFile(p, timestamp);
      try {
        Runtime.getRuntime().exec(new String[] {"bash","-c","qsub -k oe -N Ont_Pipeline_" + timestamp + " " + p.getOutputPath() + "/pipelineWithLoop_" + timestamp + ".pbs" });
      } catch (Exception e) {
        logger.error("Can not run PBS file. " + e);
      }
      pUtil.createAlertDialog(AlertType.INFORMATION, "Submitted.", "Your job has been submitted successfully.");
      getQstat();
      try {
        Runtime.getRuntime().exec(new String[] {"bash","-c","gnome-terminal -- sh -c 'tail -F /home/sysgen/Ont_Pipeline_" + timestamp + ".o*'" });
      } catch (Exception e) {
        logger.error("Can not open terminal to show log. " + e);
      }
    }
    
  }
     
  /**
   * Called when select Nanopore reads workspace button is clicked.
   * Error check:
   * No FAST5 or FASTQ file is found in the directory.
   */
  @FXML
  private void handleSelectNanoporeWorkspace() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    File selectedDirectory = directoryChooser.showDialog(null);
    if (selectedDirectory != null) {
      if (pUtil.checkDirectoryValidity(selectedDirectory,"fast5") || pUtil.checkDirectoryValidity(selectedDirectory,"fastq")) {
        tfNanoporeWorkspace.setText(selectedDirectory.toString()); 
      } else {
        pUtil.createAlertDialog(AlertType.ERROR, "Wrong ONT workspace.", "No FAST5/FASTQ files found in this directory.");
      }
    } 
  }
  
  /**
   * Called when select Illumina reads workspace button is clicked.
   * Error check:
   * The name structure is wrong.
   */
  @FXML
  private void handleSelectIlluminaWorkspace() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    File selectedDirectory = directoryChooser.showDialog(null);
    if (selectedDirectory != null) {
      if (pUtil.checkIlluminaReads(selectedDirectory)) {
        tfIlluminaWorkspace.setText(selectedDirectory.toString()); 
        if (pUtil.getIlluminaReadsPrefix(selectedDirectory).size() == 1) {
          tfPrefix.setText(pUtil.getIlluminaReadsPrefix(selectedDirectory).get(0));
        } else {
          tfPrefix.setText("");
        }
      } else {
        pUtil.createAlertDialog(AlertType.ERROR, "Wrong Illumina directory.", "This directory is not valid.");
        tfIlluminaWorkspace.setText(""); 
        tfPrefix.setText("");
      }
    }
  }
  
  /**
   * Called when select output directory button is clicked.
   */
  @FXML
  private void handleSelectOutputDirectory() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    File selectedDirectory = directoryChooser.showDialog(null);
    if (selectedDirectory != null) {
      tfOutputPath.setText(selectedDirectory.toString());
    }
  }
  
  /**
   * Opens the document in a browser.
   */
  @FXML
  private void handleOpenDocument() {
    try {
      new ProcessBuilder("x-www-browser", "https://ontpipeline2.readthedocs.io/").start();
    } catch (Exception e) {
      logger.error("Can not open document. " + e);
    }
  }
  
  /**
   * Reads the sample sheet.
   * Error check:
   * The name structure in the sample sheet is wrong.
   */
  @FXML
  private void handleReadSampleSheet() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("CSV/TSV Files", "*.csv", "*.CSV", "*.tsv", "*.TSV"));
    File sampleSheetFile = fileChooser.showOpenDialog(null);
    if (sampleSheetFile != null) {
      String sSampleSheet = sampleSheetFile.toString();
      String sExtension = pUtil.getFileExtension(sSampleSheet);
      if (pUtil.checkSampleSheet(sSampleSheet, sExtension)) {
        tfSampleSheet.setText(sSampleSheet);
        p.setSampleSheetContent(pUtil.formatSampleSheetContent(pUtil.getSampleSheetContent(sSampleSheet, sExtension)));
      } else {
        pUtil.createAlertDialog(AlertType.ERROR, "Wrong sample sheet.", "The format of sample sheet is wrong.");
      };
    }
  }
  
  /**
   * Gets the result from qstat command.
   */
  @FXML
  private void getQstat() {
    ArrayList<String> alResult = pUtil.getQstat();
    String s = "";
    for (int i=0;i<alResult.size();i++ ) {
      s = s + alResult.get(i) + "\n";
    }
    s = "Job status:\n" + ( s.isEmpty() ? "No jobs." : s );
    taQstat.setText(s);
  }
}