# datchain
Computer Science project, 2nd semester AAU. Aims to create a framework for a distributed and public registry of persons based on Proof-of-Authority. 
This type of distributed concensus diverges from research by [POA Network](https://github.com/poanetwork) by a number of factors; no Master of Ceremony, however a genesis-authority based on governmental organ responsible for registration. 

## Setup IntelliJ-environment
For using the IDE-environment to compile, debug and build artifacts, the following setup must be carried out:

1. Clone or extract repository from archive to local filesystem. This directory will be referred to as `/` or root going forward.
2. Open IntelliJ and open project by going to `File -> Open...` and navigate to root. Select `/datchain/` and press `OK`
3. Go to `Project Structure` by keyboard shortcut `Ctrl + Alt + Shift + S`
4. Go to `Project` and set `Project SDK` to local Java 9 SDK or above. If your flavour of IntelliJ ships Java SDK 9 or above, this is compatible as well.
5. Go to `Libraries` and add libraries `fuzzywuzzy-1.1.10.jar` and `gson-2.8.4.jar` included in `/datchain/lib/` by pressing the plus-icon.
   - If you wish to compile your own artifacts, `*.jar`s, go to `Artifacts` and press the plus-icon, choose `JavaFx Application` and go to tab `Java FX` in the new entry. Set `Application class` to `dk.aau.cs.a311c.datchain.gui.Wrapper`.
6. Go to `Run/Debug Configurations` and add a new `Application` through the plus-icon. Under `Main class` choose `dk.aau.cs.a311c.datchain.Datchain` and optionally specify `JRE` to either `9` or `10`.
7. Now project can be compiled and run within IntelliJ through choosing newly created run-configuration and using toolbar-icons.

## Setup unit testing in IntelliJ-environment
To run `JUnit5` tests on the project, the following setup must be carried out:

0. Make sure your IntelliJ-environment is setup correctly according to guide above and if you are, at any point, prompted to download `JUnit5` from Maven-repositories, accept and `JUnit5` will be added to project libraries.
1. Go to `Project Structure` by keyboard shortcut `Ctrl + Alt + Shift + S`.
2. Go to `Modules` and in tab `Sources` mark folder `datchain/test/` as `Tests` and apply changes.
3. Go to `Run/Debug Configurations` and add new `JUnit` optionally setting new `Name`.
4. In new entry's `Configuration` tab, set `Test kind` to `All in directory` from drop-down menu.
5. Set directory to `/datchain/test/` leaving default settings unchanged and apply changes.
6. Now tests and coverage can be run within IntelliJ through choosing newly created run-configuration and using toolbar-icons.

## Documentation
Documentation on means of Proof-of-Authority distributed concensus: [Consensus.md](notes/Consensus.md)


