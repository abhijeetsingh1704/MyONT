Reads Filter Settings
=====================

.. image:: /img/ReadsFilterSettings.png

.. image:: /img/AdvancedReadsFilterSettings.png

Porechop Settings [1]_ (Optional)
_________________________________
Set Porechop options.

.. note::
  * Select "I want to trim adapter" if you want to use Porechop to trim adapters. Defaut: selected.
  * Select "I want to skip splitting reads based on middle adapters" if you do not want to splt reads when an adapter is found in the middle. Default: not selected. 

Read Score [2]_ (Required)
_____________________________
Set a minimum average read quality score to filter the reads.

.. note::
  * Default: 9.

Read Length [2] (Required)
______________________________
Set a minimum read length to filter reads.

.. note::
  * Default: 500.

Head Crop [2]_ (Required)
____________________________
Set n nucleotides to be trimmed from start of read.

.. note::
  * Default: 50. 
 
.. [1] Porechop https://github.com/rrwick/Porechop
.. [2] NanoFilt https://github.com/wdecoster/nanofilt