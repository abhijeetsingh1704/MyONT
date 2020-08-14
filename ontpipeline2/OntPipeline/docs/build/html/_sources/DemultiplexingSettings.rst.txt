Demultiplexing Settings
=======================

.. image:: /img/DemultiplexingSettings.png

Barcode kit [1]_ (Optional)
______________________________
Choose the barcode kit(s) from the list if used.

.. note::
  * If no barcode kit was used, leave it blank.
  * Multiple selections possible.
  
records_per_fastq [1]_ (Default)
_________________________________

.. note::
  * Set value: 0.
  * Use a single file (per worker, per run id).

recursive [1]_ (Default)
_________________________

.. note::
  * Set value: search for input files recursively.
  
trim_barcodes [2]_ (Default)
____________________________

.. note::
  * Trim the barcodes from the output sequences in the FASTQ files.

 
.. [1] How to configure Guppy parameters https://community.nanoporetech.com/protocols/Guppy-protocol-preRev/v/gpb_2003_v1_revg_14dec2018/how-to-configure-guppy-parameters
.. [2] Guppy update (v3.1.5) https://community.nanoporetech.com/posts/guppy-update-v3-1-5