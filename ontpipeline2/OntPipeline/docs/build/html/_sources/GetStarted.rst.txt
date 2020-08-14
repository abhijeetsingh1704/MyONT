Installation
============
Installation
_______________________________
Anaconda Installation
^^^^^^^^^^^^^^^^^^^^^
Installing on Linux https://docs.anaconda.com/anaconda/install/linux/

.. note::
  * Anaconda is installed in :file:`/opt` directory .

JDK8 Installation [9]_
^^^^^^^^^^^^^^^^^^^^^^
1. Download source pakage from Oracle. https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html 


2. Extract JDK8 files to the target folder.

.. code-block:: bash

  sudo mkdir /usr/lib/jvm
  sudo tar -zxvf jdk-8u211-linux-x64.tar.gz -C /usr/lib/jvm
  
3. Set environment variables for JDK8.

.. code-block:: bash

  sudo nano ~/.bashrc
  #set oracle jdk environment
  export JAVA_HOME=/usr/lib/jvm/jdk-1.8.0_211
  export JRE_HOME=${JAVA_HOME}/jre  
  export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib  
  export PATH=${JAVA_HOME}/bin:$PATH
  #make changes take effect immediately
  source ~/.bashrc

4. Set JDK8 to jdk-1.8.0_211.

.. code-block:: bash
 
  sudo update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk-1.8.0_211/bin/java 300  
  sudo update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/jdk-1.8.0_211/bin/javac 300  
  sudo update-alternatives --install /usr/bin/jar jar /usr/lib/jvm/jdk-1.8.0_211/bin/jar 300   
  sudo update-alternatives --install /usr/bin/javah javah /usr/lib/jvm/jdk-1.8.0_211/bin/javah 300   
  sudo update-alternatives --install /usr/bin/javap javap /usr/lib/jvm/jdk-1.8.0_211/bin/javap 300
  #set path to jdk-1.8.0_211 
  sudo update-alternatives --config java 
  
5. Test

.. code-block:: bash
  
  java -version
  # The following messages should be showed if it works.
  java version "1.8.0_211"
  Java(TM) SE Runtime Environment (build 1.8.0_211-b12)
  Java HotSpot(TM) 64-Bit Server VM (build 25.211-b12, mixed mode)

Guppy Installation
^^^^^^^^^^^^^^^^^^

Guppy is a data processing toolkit that contains the Oxford Nanopore Technologies' basecalling algorithms, and several bioinformatic post-processing features. [1]_

.. code-block:: bash

  wget https://mirror.oxfordnanoportal.com/software/analysis/ont-guppy-cpu_3.0.3_linux64.tar.gz
  tar -xf ont-guppy-cpu_3.0.3_linux64.tar.gz
  sudo mv ont-guppy-cpu_3.0.3_linux64 /opt/ont-guppy-cpu_3.0.3

Porechop Installation
^^^^^^^^^^^^^^^^^^^^^

Porechop is a tool for finding and removing adapters from Oxford Nanopore reads. [2]_

.. code-block:: bash
   
   /opt/anaconda3/bin/conda create -n porechop
   source /opt/anaconda3/bin/activate porechop
   conda install -c bioconda porechop
   conda deactivate

NanoStat Installation
^^^^^^^^^^^^^^^^^^^^^

NanoStat calculates various statistics from a long read sequencing dataset in fastq, bam or albacore sequencing summary format. [3]_

.. code-block:: bash

   /opt/anaconda3/bin/conda create -n nanostat
   source /opt/anaconda3/bin/activate nanostat
   conda install -c bioconda nanostat
   conda deactivate

NanoFilt Installation
^^^^^^^^^^^^^^^^^^^^^

NanoFilt filters and trims long read sequencing data. [4]_

.. code-block:: bash

   /opt/anaconda3/bin/conda create -n nanofilt
   source /opt/anaconda3/bin/activate nanofilt
   conda install -c bioconda nanofilt
   conda deactivate


Unicycler Installation
^^^^^^^^^^^^^^^^^^^^^^

Unicycler is an assembly pipeline for bacterial genomes. [5]_

.. code-block:: bash
   
   /opt/anaconda3/bin/conda create -n unicycler
   source /opt/anaconda3/bin/activate unicylcer
   conda install -c bioconda unicycler
   conda install -c bioconda bcftools # for .vcf file
   conda deactivate
   
.. warning::
  * Change the memory setting in Pilon is necessary or it could be failed to start [10]_ .

BUSCO Installation
^^^^^^^^^^^^^^^^^^

BUSCO v3 provides quantitative measures for the assessment of genome assembly, gene set, and transcriptome completeness, based on evolutionarily-informed expectations of gene content from near-universal single-copy orthologs selected from OrthoDB v9. [6]_

.. code-block:: bash
   
   /opt/anaconda3/bin/conda create -n busco
   source /opt/anaconda3/bin/activate busco
   conda install -c bioconda busco
   conda deactivate

BWA Installation
^^^^^^^^^^^^^^^^

BWA is a software package for mapping low-divergent sequences against a large reference genome. [7]_

.. code-block:: bash
   
   /opt/anaconda3/bin/conda create -n bwa
   source /opt/anaconda3/bin/activate bwa
   conda install -c bioconda bwa
   conda deactivate

Seqtk Installation
^^^^^^^^^^^^^^^^^^

Seqtk is a fast and lightweight tool for processing sequences in the FASTA or FASTQ format. [8]_

.. code-block:: bash
   
   /opt/anaconda3/bin/conda create -n seqtk
   source /opt/anaconda3/bin/activate seqtk
   conda install -c bioconda seqtk
   conda deactivate
   
Trimmomatic Installation
^^^^^^^^^^^^^^^^^^^^^^^^

Trimmomatic is a flexible read trimming tool for Illumina NGS data. [11]_

.. code-block:: bash
   
   /opt/anaconda3/bin/conda create -n trimmomatic
   source /opt/anaconda3/bin/activate trimmomatic
   conda install -c bioconda trimmomatic
   conda deactivate

.. [1] Guppy v3.0.3 Release https://community.nanoporetech.com/posts/guppy-3-0-release
.. [2] Porechop https://github.com/rrwick/Porechop
.. [3] NanoStat https://github.com/wdecoster/nanostat
.. [4] NanoFilt https://github.com/wdecoster/nanofilt
.. [5] Unicycler https://github.com/rrwick/Unicycler
.. [6] BUSCO v3 https://busco.ezlab.org
.. [7] BWA https://github.com/lh3/bwa
.. [8] Seqtk https://github.com/lh3/seqtk
.. [9] Ubuntu 安装 JDK 7 / JDK8 的两种方式 https://www.cnblogs.com/a2211009/p/4265225.html
.. [10] Pilon step runs out of error https://github.com/rrwick/Unicycler/issues/147
.. [11] Trimmomatic http://www.usadellab.org/cms/?page=trimmomatic