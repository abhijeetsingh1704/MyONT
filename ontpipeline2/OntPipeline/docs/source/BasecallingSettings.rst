Base Calling Settings
=====================

.. image:: /img/BasecallingSettings.png

Flowcell ID [2]_ (Required)
______________________________
Choose the Flowcell ID from the select list.

Kit Number [2]_ (Required)
_____________________________
Choose the kit number from the select list.

.. image:: /img/AdvancedBasecallingSettings.png

Mode (Required)
________________
Set the Guppy base calling mode.

.. note::
  * Default: high-accuracy.
  
Device (Required)
_________________
Set the sequencing device.

.. note::
  * Default: PromethION.

cpu_threads_per_caller [1]_ (Default)
_____________________________________

.. note::
  * Set value: 1.

records_per_fastq [2]_ (Default)
_________________________________
.. note::
  * Set value: 0.
  * Use a single file (per worker, per run id).

recursive [2]_ (Default)
_________________________
.. note::
  * Set value: search for input files recursively.

.. [1] Guppy v3.0.3 Release https://community.nanoporetech.com/posts/guppy-3-0-release
.. [2] How to configure Guppy parameters https://community.nanoporetech.com/protocols/Guppy-protocol-preRev/v/gpb_2003_v1_revg_14dec2018/how-to-configure-guppy-parameters