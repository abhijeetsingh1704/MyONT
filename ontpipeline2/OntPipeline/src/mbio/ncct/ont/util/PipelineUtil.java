package mbio.ncct.ont.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import mbio.ncct.ont.model.Pipeline;

/**
 * This is the PipelineUtil class for utilities used in Pipeline.
 * 
 * @author Yan Zhou
 * created on 2019/05/16
 */
public class PipelineUtil {
  
  /** Initializes log4j2. */
  private static Logger logger = LogManager.getLogger(PipelineUtil.class);
  
  /** Sets Guppy location. */
  private static String guppyUrl = "/opt/ont-guppy-cpu_3.1.5";
  
  /** Sets pbs file location. */
  //private static String pbsUrl = "/opt/ontpipeline/pbs/pipelineWithLoop.pbs";
  private static String pbsUrl = "/opt/ontpipeline/pbs/pipelineWithLoop.pbs";
  
  /**
   * Gets all flowcell IDs.
   * @return a String Array with all flowcell IDs. 
   */
  public ArrayList<String> getFlowcellIds() {
    String s = null;
    ArrayList<String> arFlowcellIds = new ArrayList<String>();
    Process p = null;
    try {
      p = Runtime.getRuntime().exec(new String[] { "bash", "-c", guppyUrl + "/bin/guppy_basecaller --print_workflows | awk 'NR>2 {print $1}' | sort | uniq" });
    } catch (Exception e) {
      logger.error("Can not get flowcell IDs. " + e);
    }
    BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
    //BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
    try {
      while ((s = stdInput.readLine()) != null ) {
        if (!s.isEmpty()) {
          arFlowcellIds.add(s);
        }
      }
    } catch (Exception e) {
      logger.error("Can not read flowcell IDs. " + e);
    }
    return arFlowcellIds;
  }
  
  /**
   * Gets all kit numbers.
   * @return a String Array with all kit numbers.
   */
  public ArrayList<String> getKitNumbers() {
    String s = null;
    ArrayList<String> arKitNumbers = new ArrayList<String>();
    Process p = null;
    try {
      p = Runtime.getRuntime().exec(new String[] { "bash", "-c", guppyUrl + "/bin/guppy_basecaller --print_workflows | awk 'NR>2 {print $2}' | sort | uniq" });
    } catch (Exception e) {
      logger.error("Can not get kit numbers. " + e);
    }
    BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
    //BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
    try {
      while ((s = stdInput.readLine()) != null ) {
        if (!s.isEmpty()) {
          arKitNumbers.add(s);
        }
      }
    } catch (Exception e) {
      logger.error("Can not read kit numbers. " + e);
    }
    return arKitNumbers;
  }
  
  /**
   * Gets all the combinations of flowcell ID and kit number.
   * @return a map with all the combinations of flowcell ID and kit number.
   */
  private Map<String, String> getCombinationFlowcellKit() {
    String s = null;
    Map<String, String> m = new HashMap<String, String>();
    Process p = null;
    try {
      p = Runtime.getRuntime().exec(new String[] { "bash", "-c", guppyUrl + "/bin/guppy_basecaller --print_workflows | awk 'NR>2 {print $1,$2,$3,$4}' " });
    } catch (Exception e) {
      logger.error("Can not run command: guppy_basecaller --print_workflows . " + e);
    }
    //BufferedReader stdError = null;
    try {
      BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
      //stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
      while ((s = stdInput.readLine()) != null ) {
        if (s.length() > 3) {
          String[] arr = s.replaceAll("included ", "").split(" ");
          m.put(arr[0].concat(arr[1]), arr[2]);
        }
      }
    } catch (Exception e) {
      logger.error("Can not read result from guppy_basecaller --print_workflows . " + e);
    }
    return m;
  }
  
  /**
   * Gets all barcode kits.
   * @return an Array List with all barcode kits.
   */
  public ArrayList<String> getBarcodeKits() {
    String s = null;
    ArrayList<String> arBarcodeKits = new ArrayList<String>();
    Process p = null;
    try {
      p = Runtime.getRuntime().exec(new String[] { "bash", "-c", guppyUrl + "/bin/guppy_barcoder --print_kits | awk 'NR>1 {print $1}' | sort | uniq" });
    } catch (Exception e) {
      logger.error("Can not get barcode kits. " + e);
    }
    BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
    //BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
    try {
      while ((s = stdInput.readLine()) != null ) {
        if (s.isEmpty() == false) {
          arBarcodeKits.add(s);
        }
      }
    } catch (Exception e) {
      logger.error("Can not read barcode kits. " + e);
    }
    return arBarcodeKits;
  }
  
  /**
   * Creates a .pbs file filled with the input parameters.
   * @param p A Pipeline object.
   * @param timestamp The current date and time yyyyMMdd_HHmmss.
   */
  public void createPbsFile(Pipeline p, String timestamp) {
    if ( !p.getFlowcellId().equals("FLO-MIN107") && p.getGuppyMode().equals("fast")) {
      Map<String, String> combinationFlowcellKit = null;
      String cfg = null;
      try {
        combinationFlowcellKit = getCombinationFlowcellKit();
        cfg = combinationFlowcellKit.get(p.getFlowcellId().concat(p.getKitNumber())).toString();
      } catch (Exception e) {
        logger.error("Can not get the combinations of flowcell ID and kit number. " + e);
      }
      int cfg_bps = cfg.indexOf("bps");
      String cfgFile = ( cfg.substring(0, cfg_bps + 3) + "_fast" ) + ( p.getDevice().equals("PromethION") ? "_prom" : "" ) + ".cfg";
      p.setIfGuppyFast(true);
      p.setGuppyCfgFile(guppyUrl + "/data/" + cfgFile);
    }
    
    Path path = Paths.get(pbsUrl);
    Path newPath = Paths.get(p.getOutputPath() + "/pipelineWithLoop_" + timestamp + ".pbs");
    Charset charset = StandardCharsets.UTF_8;

    String content = null;
    try {
      content = new String(Files.readAllBytes(path), charset);
    } catch (Exception e) {
      logger.error("Can not read the .pbs template. " + e);
    }
    
    content = content.replaceAll("\\$ONTWORKSPACE", p.getOntReadsWorkspace())
        .replaceAll("\\$ILLUMINAWORKSPACE", p.getIlluminaReadsWorkspace())
        .replaceAll("\\$OUTPUTPATH", p.getOutputPath())
        .replaceAll("\\$IF_BASECALLING", p.getIfBasecalling().toString())
        .replaceAll("\\$FLOWCELL_ID", p.getFlowcellId())
        .replaceAll("\\$KIT_NUMBER", p.getKitNumber())
        .replaceAll("\\$THREADS", p.getThreads())
        .replaceAll("\\$IF_DEMULTIPLEXING", p.getIfDemultiplexing().toString())
        .replaceAll("\\$SAMPLESHEET", p.getSampleSheetContent())
        .replaceAll("\\$PREFIX", p.getPrefix().isEmpty() ? "barcode" : p.getPrefix())
        .replaceAll("\\$BARCODEKIT", p.getBarcodeKits())
        .replaceAll("\\$IF_ADAPTERTRIMMING", p.getIfAdapterTrimming().toString())
        .replaceAll("\\$BARCODENUMBERS", p.getSelectedBarcode().isEmpty() ? "" : formatSelectedBarcodes(p.getSelectedBarcode()))
        .replaceAll("\\$IF_READSFILTER", p.getIfReadsFilter().toString())
        .replaceAll("\\$SCORE", p.getReadScore())
        .replaceAll("\\$LENGTH", p.getReadLength())
        .replaceAll("\\$HEADCROP", p.getHeadCrop())
        .replaceAll("\\$IF_ASSEMBLY", p.getIfAssembly().toString())
        .replaceAll("\\$IF_VCF", p.getIfVcf().toString())
        .replaceAll("\\$MODE", p.getMode())
        .replaceAll("\\$METHOD", p.getMethod())
        .replaceAll("\\$IF_POLISHING", p.getIfPolishing().toString())
        .replaceAll("\\$IF_BUSCO", p.getIfBusco().toString())
        .replaceAll("\\$LINEAGE", p.getBuscoData())
        .replaceAll("\\$PTIMES", p.getPtimes())
        .replaceAll("\\$IF_GUPPYFAST", p.getIfGuppyFast().toString())
        .replaceAll("\\$CFG_FILE", p.getGuppyCfgFile());
   try {
      Files.write(newPath, content.getBytes(charset));
    } catch (Exception e) {
      logger.error("Can not write .pbs file. " + e);
    }
  }
  
  /**
   * Creates an user log file with all the input parameters.
   * @param p a Pipeline object.
   * @param timestamp the current date and time: yyyyMMdd_HHmmss.
   */
  public void createUserLog(Pipeline p, String timestamp) {
    String path = p.getOutputPath() + "/userlog_" + timestamp + ".log";
    File f = new File(path);
    f.getParentFile().mkdirs(); 
    try {
     f.createNewFile();
    } catch (Exception e) {
      logger.error("Can not create user log file. " + e);
    }
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(f, true));
      writer.append("====General Settings====\n");
      writer.append("Nanopore reads directory: " + p.getOntReadsWorkspace() + "\n");
      writer.append("Illumina reads directory: " + (p.getIlluminaReadsWorkspace().isEmpty() ? "Not given." : p.getIlluminaReadsWorkspace()) + "\n");
      writer.append("Output directory: " + p.getOutputPath() + "\n");
      writer.append("Sample sheet path: " + (p.getSampleSheetPath().isEmpty() ? "Not given." : p.getSampleSheetPath()) + "\n");
      writer.append("Prefix: " + (p.getPrefix().isEmpty() ? "Not given." : p.getPrefix()) + "\n");
      writer.append("Threads: " + p.getThreads() + "\n");
      writer.append("Selected barcodes: " + ( p.getSelectedBarcode().isEmpty() ? "Default: all. " :formatSelectedBarcodes(p.getSelectedBarcode()) ) + "\n");
      if (p.getIfBasecalling()) {
        writer.append("\n====Basecalling Settings====\n");
        writer.append("Flowcell ID: " + p.getFlowcellId() + " \n");
        writer.append("Kit number: " + p.getKitNumber() + " \n");
        writer.append("Guppy mode: " + p.getGuppyMode() + " \n");
        writer.append("Device: " + p.getDevice() + " \n");
      } else {
        writer.append("\n====No Basecalling====\n");
      }
      if (p.getIfDemultiplexing()) {
        writer.append("\n====Demultiplexing Settings====\n");
        writer.append("Barcode kits: " + (p.getBarcodeKits().isEmpty() ? "" : p.getBarcodeKits()) + " \n"); 
      } else {
        writer.append("\n====No Demultiplexing====\n");
      }
      if (p.getIfReadsFilter()) {
        writer.append("\n====Reads Filter Settings====\n");
        writer.append("Read score: " + p.getReadScore() + " \n");
        writer.append("Read length: " + p.getReadLength() + " \n");
        writer.append("Head crop: " + p.getHeadCrop() + " \n");
        writer.append("If adapter trimming: " + p.getIfAdapterTrimming() + " \n");
        if (p.getIfAdapterTrimming()) {
          writer.append("If split reads: " + p.getIfNoSplit() + "\n"); 
        }
      } else {
        writer.append("\n====No Reads Filter====\n");
      }
      if (p.getIfAssembly()) {
        writer.append("\n====Assembly Settings====\n");
        writer.append("Assembly mode: " + p.getMode() + " \n");
        writer.append("Assembly method: " + p.getMethod() + " \n");
        writer.append("VCF: " + p.getIfVcf() + " \n");
      } else {
        writer.append("\n====No Assembly====\n");
      }
      if (p.getIfPolishing()) {
        writer.append("\n====Polishing Settings====\n");
        writer.append("Polishing times: " + p.getPtimes() + " \n");
        writer.append("BUSCO: " + p.getIfBusco() + " \n");
        if (p.getIfBusco()) {
          writer.append("BUSCO database: " + p.getBuscoData() + " \n"); 
        }
      } else {
        writer.append("\n====No Polishing====\n");
      }
      writer.close();
    } catch (Exception e) {
      logger.error("Can not create user log file. " +  e);
    }
  }
  
  /**
   * Reads sample sheet and parses the content.
   * @param sampleSheet the String of sample sheet file path.
   * @param extension the String of sample sheet file extension.
   * @return the HashMap of sample sheet content.
   */
  public HashMap<String,String> getSampleSheetContent(String sampleSheet, String extension) {
    HashMap<String,String> hmResult = new HashMap<String,String>();
    String ch = (extension.toLowerCase().equals("csv") ? "," : "\t");
    try (BufferedReader br = new BufferedReader(new FileReader(sampleSheet))) {
      String line;
      line = br.readLine();
      while ((line = br.readLine()) != null) {
        String[] values = line.split(ch);
        hmResult.put("barcode"+values[1].substring(values[1].length()-2), values[0]);
      }
    } catch (Exception e) {
      logger.error("Can not read sample sheet." + e);
    } 
    return hmResult;
  }
  
  /**
   * Formats the HashMap of sample sheet content to String.
   * @param hmSampleSheet the sample sheet content in HashMap format.
   * @return formatted String of sample sheet content (HashMap in Bash).#(['barcode01']='MW_1' ['barcode02']='MW23' ['barcode03']='AA_45' )
   */
  public String formatSampleSheetContent(HashMap<String,String> hmSampleSheet) {
    String result = "";
    for(Map.Entry<String, String> entry : hmSampleSheet.entrySet()) {
      result = result + "['" + entry.getKey() + "']='" + entry.getValue() + "' ";  
    }
    return "(" + result + ")";
  }
  
  /**
   * Checks if the sample sheet has the correct content.
   * @param sampleSheet the String of sample sheet file path
   * @param extension the String of the sample sheet extension.
   * @return the Boolean value if the sample sheet correct is.
   */
  public Boolean checkSampleSheet(String sampleSheet, String extension) {
    Boolean result = true;
    ArrayList<String> sampleNames = new ArrayList<String>();
    ArrayList<String> barcodeNames = new ArrayList<String>();
    String ch = (extension.equals("csv".toLowerCase()) ? "," : "\t");
    try (BufferedReader br = new BufferedReader(new FileReader(sampleSheet))) {
      String line;
      line = br.readLine();
      while ((line = br.readLine()) != null) {
        String[] values = line.split(ch);
        if (!values[1].substring(values[1].length() - 2).matches(".*\\d\\d") || sampleNames.contains(values[0]) || barcodeNames.contains(values[1])) {
          result = false;
          break;
        } else {
          sampleNames.add(values[0]);
          barcodeNames.add(values[1].substring(values[1].length() - 2)); 
        }
      }
    } catch (Exception e) {
      logger.error("Can not read sample sheet." + e);
    }
    return result;
  }
  
  /**
   * Creates an alert dialog.
   * @param alertType the alert type.
   * @param alertTitle the dialog title.
   * @param alertContent the dialog content.
   */
  public void createAlertDialog(AlertType alertType, String alertTitle, String alertContent) {
    Alert alert = new Alert(alertType);
    alert.setTitle(alertTitle);
    alert.setContentText(alertContent);
    alert.showAndWait();
  }
  
  /**
   * Formats the String of selected barcodes.
   * @param selectedBarcode selected barcodes.
   * @return formatted String of selected barcodes.
   */
  private String formatSelectedBarcodes(String selectedBarcode) {
    String result = null;
    if (!selectedBarcode.isEmpty()) {
      String[] strArr = selectedBarcode.split(",");
      String formattedSelectedBarcode = "";
      for(int i=0; i<strArr.length; i++) {
        String formatted = String.format("%02d", Integer.valueOf(strArr[i])) + ",";
        formattedSelectedBarcode = formattedSelectedBarcode + formatted;
      }
      result = "barcode{" + formattedSelectedBarcode.substring(0, formattedSelectedBarcode.length()-1) + "}/";
    }
    return result;
  }
  
  /**
   * Checks if the directory has the correct files with the given extension.
   * @param selectedDirectory the given directory to be checked.
   * @param extension the given extension to be checked in the directory
   * @return the Boolean value if the directory is valid.
   */
  public Boolean checkDirectoryValidity(File selectedDirectory, String extension) {
    boolean result = false;
    File[] f = selectedDirectory.listFiles();
    for (int i = 0; i < f.length; i++) {
      if (f[i].isFile() && getFileExtension(f[i].getName()).toLowerCase().equals(extension)) {
        result = true;
        break;
      } 
    }
    return result;
  }
  
  /**
   * Gets the extension of a given file.
   * @param fileName the String of file name.
   * @return the String of extension of a given file.
   */
  public String getFileExtension(String fileName) {
    String extension = "";
    int i = fileName.lastIndexOf('.');
    if (i > 0) {
      extension = fileName.substring(i+1);
    }
    return extension;
  }
  
  /**
   * Checks the Illumina reads directory.
   * @param illuminaDirectory the path to the Illumina reads directory.
   * @return the Boolean value if the Illumina reads directory is valid. 
   */
  public Boolean checkIlluminaReads(File illuminaDirectory) {
    Boolean validity = true;
    File[] f = illuminaDirectory.listFiles();
    ArrayList<String> alFR1 = new ArrayList<String>();
    ArrayList<String> alFR2 = new ArrayList<String>();
    for (int i = 0; i < f.length; i++) {
      if (f[i].isFile() && f[i].getName().contains("_")) {
        String prefix = f[i].getName().substring(0, f[i].getName().indexOf("_"));
        if(f[i].getName().matches(".*_HQ_1\\.fastq\\.gz") && !alFR1.contains(prefix)) {
          alFR1.add(prefix);
        } else if (f[i].getName().matches(".*_HQ_2\\.fastq\\.gz") && !alFR2.contains(prefix)) {
          alFR2.add(prefix);
        } else {
          validity = false;
          break;
        }
      }
    }
    if ( !alFR1.containsAll(alFR2) || !alFR2.containsAll(alFR1) || alFR1.isEmpty()) {
      validity = false;
    }
    return validity;
  }
  
  /**
   * Gets the prefixs from the Illumina reads directory.
   * @param illuminaDirectory the path to the Illumina reads directory.
   * @return an ArrayList contains all prefixs.. 
   */
  public ArrayList<String> getIlluminaReadsPrefix(File illuminaDirectory) {
    ArrayList<String> alPrefixs = new ArrayList<String>();
    File[] f = illuminaDirectory.listFiles();
    for (int i = 0; i < f.length; i++) {
      if (f[i].isFile() && f[i].getName().matches(".*_HQ_1\\.fastq\\.gz")) {
        String prefix = f[i].getName().substring(0, f[i].getName().indexOf("_"));
        alPrefixs.add(prefix);
      }
    }
    return alPrefixs;
  }
  
  /**
   * Formats the String of barcodeKits.
   * @param barcodeKits selected barcode kits.
   * @return formatted String of barcode kits.
   */
  public String formatBarcodeKits(String barcodeKits) {
    return barcodeKits.replace("[", "\"").replace("]", "\"");
  }
  
  /**
   * Compares if the set of sample names in the sample sheet and the set of the prefixs of Illumina reads are the same.
   * @param sampleSheet the path to the sample sheet.
   * @param extension the extension of the sample sheet file
   * @param illuminaDirectory the directory of Illumina reads.
   * @return the boolean value if the two sets are the same.
   */
  public Boolean checkIlluminaReadsPrefixWithSampleSheet(String sampleSheet, String extension, File illuminaDirectory) {
    Set<String> setSampleNames = getSampleSheetContent(sampleSheet, extension).keySet();
    ArrayList<String> alSampleNames = new ArrayList<String>();
    alSampleNames.addAll(setSampleNames);
    ArrayList<String >alIlluminaPrefixs = getIlluminaReadsPrefix(illuminaDirectory);
    return alIlluminaPrefixs.containsAll(alSampleNames);
  }
  
  /**
   * Gets all prefixes of ONT reads in a given directory.
   * @param ontDirectory the path of ONT reads directory.
   * @return an ArrayList contains all prefixes.
   */
  private ArrayList<String> getOntReadsPrefix(File ontDirectory){
    ArrayList<String> alPrefixs = new ArrayList<String>();
    File[] f = ontDirectory.listFiles();
    for (int i = 0; i < f.length; i++) {
      if (f[i].isFile() && f[i].getName().matches(".*\\.fastq")) {
        String prefix = null;
        if (f[i].getName().contains("_")) {
          prefix = f[i].getName().substring(0, f[i].getName().indexOf("_")); 
        } else {
          prefix = f[i].getName().substring(0, f[i].getName().indexOf(".")); 
        }
        alPrefixs.add(prefix);
      }
    }
    return alPrefixs;
  }
  
  /**
   * Checks if the prefixes ONT reads are unique and if the set of Illumina prefixes contains all ONT reads prefixes.
   * @param ontDirectory the path of ONT reads directory.
   * @param illuminaDirectory the path of Illumina reads directory.
   * @return true if all prefixes ONT reads are unique and the set of Illumina prefixes contains all ONT reads prefixes.
   */
  public Boolean checkOntReadsPrefix(File ontDirectory, File illuminaDirectory) {
    Boolean result = true;
    ArrayList<String> alOntPrefixes = getOntReadsPrefix(ontDirectory);
    ArrayList<String> alIlluminaPrefixes = getIlluminaReadsPrefix(illuminaDirectory);
    Set<String> stOntPrefixes = new HashSet<String>(alOntPrefixes);
    if (stOntPrefixes.size() != alOntPrefixes.size() || !alIlluminaPrefixes.containsAll(alOntPrefixes)) {
      result = false;
    }
    return result;
  }
  
  /**
   * Gets the result from the qstat command.
   * @return ArrayList with the result from qstat command.
   */  
  public ArrayList<String> getQstat() {
    String s = null;
    Process p = null;
    ArrayList<String> alResult = new ArrayList<String>();
    try {
      p = Runtime.getRuntime().exec(new String[] { "bash", "-c", "qstat" });
    } catch (Exception e) {
      logger.error("Can not get qstat result. " + e);
    }
    BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
    //BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
    try {
      while ((s = stdInput.readLine()) != null ) {
        alResult.add(s);
      }
    } catch (Exception e) {
      logger.error("Can not read qstat result. " + e);
    }
    return alResult;
  }
}