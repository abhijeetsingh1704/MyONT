Output File Structure
=====================

::

    Output_Directory/
     ├── Analysis_{timestamp}/
     |    ├── _Basecalled/
     |    ├── _Barcodes/
     |    |    ├── barcode01/
     |    |    ├── barcode02/
     |    |    ├── barcode03/
     |    |    ├── unclassified/
     |    |    ├── Prefix01.fastq
     |    |    ├── Prefix02.fastq
     |    |    └── Prefix03.fastq
     |    ├── _AdaperTrimmedFiles/
     |    |    ├── Prefix01_trimmed.fastq
     |    |    ├── Prefix02_trimmed.fastq
     |    |    └── Prefix03_trimmed.fastq
     |    ├── _FiltedFiles/
     |    |    ├── Prefix01_filted.fastq
     |    |    ├── Prefix02_filted.fastq
     |    |    └── Prefix03_filted.fastq
     |    ├── _StatFiles/
     |    |    ├── Prefix01_trimmed_stat.txt
     |    |    ├── Prefix02_trimmed_stat.txt
     |    |    ├── Prefix03_trimmed_stat.txt
     |    |    ├── Prefix01_filted_stat.txt
     |    |    ├── Prefix02_filted_stat.txt
     |    |    └── Prefix03_filted_stat.txt
     |    ├── Prefix01_Assembly/
     |    |    ├── ...
     |    |    └── assembly.fasta
     |    ├── Prefix02_Assembly/
     |    |    ├── ...
     |    |    └── assembly.fasta
     |    ├── Prefix03_Assembly/
     |    |    ├── ...
     |    |    └── assembly.fasta
     |    ├── Prefix01_Polishing/
     |    |    ├── run_Prefix01_busco/
     |    |    |    ├── ...
     |    |    |    └── full_table_Prefix01_busco.tsv
     |    |    ├── ...   
     |    |    └── pilon_1.fasta
     |    ├── Prefix02_Polishing/
     |    |    ├── run_Prefix02_busco/
     |    |    |    ├── ...
     |    |    |    └── full_table_Prefix02_busco.tsv
     |    |    ├── ...   
     |    |    └── pilon_1.fasta
     |    ├── Prefix03_Polishing/
     |    |    ├── run_Prefix03_busco/
     |    |    |    ├── ...
     |    |    |    └── full_table_Prefix03_busco.tsv
     |    |    ├── ...   
     |    |    └── pilon_1.fasta
     |    └── _Logs/
     |         ├── guppy_basecaller.log
     |         ├── guppy_barcoder.log
     |         ├── barcode_rename.log
     |         ├── Prefix01_trimmed.log
     |         ├── Prefix02_trimmed.log
     |         ├── Prefix03_trimmed.log
     |         ├── Prefix01_filted.log
     |         ├── Prefix02_filted.log
     |         ├── Prefix03_filted.log
     |         ├── Prefix01_assembly.log
     |         ├── Prefix02_assembly.log
     |         ├── Prefix03_assembly.log
     |         ├── Prefix01_polishing_1.log
     |         ├── Prefix02_polishing_1.log
     |         ├── Prefix03_polishing_1.log
     |         ├── Prefix01_busco.log
     |         ├── Prefix02_busco.log
     |         └── Prefix03_busco.log
     ├── pipelineWithLoop_{timestamp}.pbs # Submitted PBS file.
     └── userlog_{timestamp}.log # User given parameters.
     
    /home/{$USER}/
     ├── Ont_Pipeline.e* # Error messages after the run. 
     └── Ont_Pipeline.o* # Output messages after the run.
     
    /opt/ontpipeline/logs/
     ├── ...
     └── {$USER}_error.log # Error messages if something wrong with the program.
   