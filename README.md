# Multi threading demo in Java

### Prerequisites
- JDK
- docker
- docker-compose

### To generate some random files to test compressiom, run the class ```helpers.GenerateSomeText``` that will generate 5 files (file0.txt, file1.txt, file2.txt, file3.txt, file4.txt )

# Commands
- To run the app, under the app directory, run the command:
```
docker-compose up
```
To shutdown, under the app directory ,  run the command:
```
docker-compose down --rmi
```

- To get threadump for the app named DeadlockDemo, run:
```
jcmd $(pgrep -f DeadlockDemo) Thread.print
```

- To check docker cpu/memory usage, run:
```
docker stats
```
