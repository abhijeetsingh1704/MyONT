Input File Structure
====================
Start from Base Calling
_______________________
Start the pipeline from Base calling.

::
   
    ONT_Reads_Directory/
    ├── HPz800_20180821_FAJ18422_MN17776_sequencing_run_VIM4_Janina_26844_read_11_ch_171_strand.fast5
    ├── HPz800_20180821_FAJ18422_MN17776_sequencing_run_VIM4_Janina_26844_read_11_ch_203_strand.fast5
    ├── HPz800_20180821_FAJ18422_MN17776_sequencing_run_VIM4_Janina_26844_read_15_ch_344_strand.fast5   
    ├── HPz800_20180821_FAJ18422_MN17776_sequencing_run_VIM4_Janina_26844_read_17_ch_249_strand.fast5
    ├── HPz800_20180821_FAJ18422_MN17776_sequencing_run_VIM4_Janina_26844_read_19_ch_397_strand.fast5
    └── ......
    
    Illumina_Reads_Directory/
    ├── Prefix01_HQ_1.fastq.gz
    ├── Prefix01_HQ_2.fastq.gz
    ├── Prefix02_HQ_1.fastq.gz
    ├── Prefix02_HQ_2.fastq.gz
    ├── Prefix03_HQ_1.fastq.gz
    ├── Prefix03_HQ_2.fastq.gz
    └── ......

.. note::
  * Illumina reads files naming structure for each pair: Prefix_HQ_1.fastq.gz  Prefix_HQ_2.fastq.gz
  * If there is without "HQ" in the file name, these Illumina reads will be trimmed.
  * "Prefix" is the sample name, each pair should has its own prefix.
  * "*" means arbitrarily long characters.
  
.. warning::
  * Do not use underscore ("_") in the prefix.

Start from Demultiplexing
_________________________
Start the pipeline from Demultiplexing.

::
   
    ONT_Reads_Directory/
    ├── fastq_runid_50a6171cadcfb6b5cb2dae4e75a0ccc05b71e3d8_0.fastq
    ├── fastq_runid_50a6171cadcfb6b5cb2dae4e75a0ccc05b71e3d8_1.fastq
    ├── fastq_runid_50a6171cadcfb6b5cb2dae4e75a0ccc05b71e3d8_2.fastq 
    ├── fastq_runid_50a6171cadcfb6b5cb2dae4e75a0ccc05b71e3d8_3.fastq
    ├── fastq_runid_50a6171cadcfb6b5cb2dae4e75a0ccc05b71e3d8_4.fastq
    └── ......
    
    Illumina_Reads_Directory/
    ├── Prefix01_HQ_1.fastq.gz
    ├── Prefix01_HQ_2.fastq.gz
    ├── Prefix02_HQ_1.fastq.gz
    ├── Prefix02_HQ_2.fastq.gz
    ├── Prefix03_HQ_1.fastq.gz
    ├── Prefix03_HQ_2.fastq.gz
    └── ......


Start from Reads Filter
_______________________
Start the pipeline from Reads Filter.

::

    ONT_Reads_Directory/
    ├── Prefix01.fastq
    ├── Prefix02.fastq
    ├── Prefix03.fastq 
    ├── Prefix04.fastq
    ├── Prefix05.fastq
    └── ......
    
    Illumina_Reads_Directory/
    ├── Prefix01_HQ_1.fastq.gz
    ├── Prefix01_HQ_2.fastq.gz
    ├── Prefix02_HQ_1.fastq.gz
    ├── Prefix02_HQ_2.fastq.gz
    ├── Prefix03_HQ_1.fastq.gz
    ├── Prefix03_HQ_2.fastq.gz
    └── ......

Start from Assembly
___________________
Start the pipeline from Assembly.

::
   
    ONT_Reads_Directory/
    ├── Prefix01.fastq
    ├── Prefix02.fastq
    ├── Prefix03.fastq 
    ├── Prefix04.fastq
    ├── Prefix05.fastq
    └── ......
    
    Illumina_Reads_Directory/
    ├── Prefix01_HQ_1.fastq.gz
    ├── Prefix01_HQ_2.fastq.gz
    ├── Prefix02_HQ_1.fastq.gz
    ├── Prefix02_HQ_2.fastq.gz
    ├── Prefix03_HQ_1.fastq.gz
    ├── Prefix03_HQ_2.fastq.gz
    └── ......
    
Start from Polishing
____________________
Start the pipeline from Polishing.

::
   
    ONT_Reads_Directory/
    ├── Prefix01.fasta
    ├── Prefix02.fasta
    ├── Prefix03.fasta 
    ├── Prefix04.fasta
    ├── Prefix05.fasta
    └── ......
    
    Illumina_Reads_Directory/
    ├── Prefix01_HQ_1.fastq.gz
    ├── Prefix01_HQ_2.fastq.gz
    ├── Prefix02_HQ_1.fastq.gz
    ├── Prefix02_HQ_2.fastq.gz
    ├── Prefix03_HQ_1.fastq.gz
    ├── Prefix03_HQ_2.fastq.gz
    └── ......

Sample Sheet
____________
.. csv-table:: Sample Sheet
   :header: Sample,Barcode
   :widths: 20, 20

   example1,barcode01
   example2,barcode02
   example3,barcode03
   example4,barcode04
   example5,barcode05
   
.. note::
  * The type of sample sheet file is CSV (split cell contents by comma) or TSV (split cell contents by tab).
  * The format of barcode name: barcodeXX ("barcode" can be any characters, but XX must be two digits: 01,02,03,...,10,11,12,...)