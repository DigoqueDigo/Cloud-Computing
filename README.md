# Nimbus

A distributed cloud computing system built on a client-server architecture. It allows users to submit computational jobs that are intelligently scheduled and executed across a dynamic pool of connected machines. Designed with scalability and concurrency in mind, *Nimbus* enables efficient resource management and responsive job handling in a distributed environment.

---

## 🧩 Components

The system includes three main components:

- **Server:** coordinates the system by managing user sessions, machine resources, and job scheduling.
- **Client:** allows users to submit jobs and retrieve processed results.
- **Machine:** represents a worker node capable of executing tasks based on available memory.

---

## 🛠️ Compilation

Ensure that all source files are compiled into the `bin/` directory. External dependencies (such as `sd23.jar`) should be placed in the `lib/` directory.

---

## 🚀 Running the System

### Start the Server

```
java -cp bin server.Server
```

### Launch Client

```
java -cp bin client.user.UserClient <input_folder> <output_folder>
```

- **input_folder:** Directory containing input files for processing.
- **output_folder:** Directory where output results will be saved.

### Launch Machine 

```
java -cp bin:lib/sd23.jar client.machine.MachineClient <machine_memory>
```

- **machine_memory:** Amount of memory available for processing jobs (integer).

---

## ⚙️ Features

- Dynamic Job Scheduling
  - Takes into account job memory requirements and user-defined tolerance levels
  - Prevents job starvation while ensuring efficient resource usage
- Concurrent Multi-Client & Multi-Machine Support
  - Fully multi-threaded I/O handling
  - Independent result processing and execution threads
