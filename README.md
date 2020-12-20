# ZigbeeCardConfigurator

ZigBee Configuration tool is a software application that provides simple interface to configure and sniff data on the ZigBee network (that uses TI’s CC2530 or CC2531).

The key concept in this design is to create simple interface for technicians to engineers to quickly access and configure the ZigBee network and provide secure environment to configuration.


To make sure that the RXTX binaries are found, update the VM options in the Run settings when running from Netbeans.

Example for windows:
```
-Dgnu.io.log.mode=PRINT_MODE -Djava.library.path=dist/lib/rxtx-2.2pre2-bins/Windows/win64/
```

The "dist" directory does not seem to be filled correctly.

The project has been updated with a dark theme.


The project does not handle a filled RX buffer very well - multiple packets may be in the buffer, and only the first is read.

For some reason the status refresh is not working.  Possibly because of thread limitations (is the receive buffer filled while the thread is sleeping?).
