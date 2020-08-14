General Settings
================

.. image:: /img/GeneralSettings.png

ONT Dir. (Required)
___________________
Set the path to the Nanopore reads directory. 

.. note::
  * **Example:** :file:`/path/to/your/ONT/reads/directory`
  
Illumina Dir. (Optional/Required)
_________________________________
Set the path to the Illumina reads directory. 

.. note::
  * **Example:** :file:`/path/to/your/Illumina/reads/directory`
  * Required if "hybrid-assembly" or/and "polishing" is/are used.

.. warning::
  * If the structure of Illumina reads filename is Prefix_{1,2}.fastq.gz (for example: ID40_1.fastq.gz, ID40_2.fastq), these Illumina reads will be trimmed.
  * If the structure of Illumina reads filename is Prefix_HQ_{1,2}.fastq.gz (for example: ID40_HQ_1.fastq.gz, ID40_HQ_2.fastq), these Illumina reads will not be trimmed.

Output Dir. (Required)
______________________
Set the path to the output directory. 

.. note::
  * **Example:** :file:`/path/to/your/output/directory`

Sample sheet (Optional)
_______________________
Set the path to the sample sheet file. 

.. note::
  * The sample sheet file type must be CSV or TSV.

.. warning::
  * Underscore('_') is **not** allowed in the sample name.
  
Prefix (Optional)
_________________
Set the prefix for the Nanopore reads after demultiplexing. 

.. note::
  * **Example:** ID .
  * Default: barcode .

Threads (Required)
_____________________
Set the number of threads/cpus to run the analysis.

.. note::
  * Default: 8.

Barcodes (Optional)
______________________
Set which barcodes will be analyzed. Put in the numbers and separate them with a comma.

.. note::
  * **Example:** 1,2,3,4
  * If you want to analysis all barcodes, leave it blank.