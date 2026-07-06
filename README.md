# 🚀 Mission Dispatcher Pro (Generic Power-of-Two Max Heap)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Algorithms](https://img.shields.io/badge/Algorithms-Data_Structures-blue?style=for-the-badge)
![Clean Code](https://img.shields.io/badge/Clean_Code-S.O.L.I.D-brightgreen?style=for-the-badge)

A high-performance, object-oriented **Mission Dispatcher** console application. 
At its core, this project demonstrates the implementation of a custom **Generic D-ary (Power-of-Two) Max Heap** to handle priority queues with blazing speed and memory efficiency.

---

## 📸 See it in Action

> **Note:** Here is how the dispatcher looks while actively resolving missions in the terminal.

*[UBACI_SLIKU_OVDJE]* *(Izbriši ovaj tekst i samo drag&drop screenshot tvog terminala dok program radi)*

---

## 🌟 Key Features & Engineering Highlights

- **Generic Data Structure (`<T extends Comparable<T>>`):** 
  The core Heap algorithm is highly decoupled. It is not limited to "Missions" and can reliably process any object class that implements the `Comparable` interface.
  
- **Mathematical Bitwise Optimization:** 
  Instead of standard multiplication/division, the engine leverages **bitwise shifts** (`1 << k`, `>> k`) to calculate parent and child array indices. This significantly accelerates CPU cycles during the `siftUp` and `siftDown` operations.

- **S.O.L.I.D. Principles & Architecture:** 
  The codebase separates business logic from data structures and presentation:
  - `PowerOfTwoMaxHeap`: Handles strict sorting algorithms.
  - `MissionDispatcher`: Acts as the domain controller.
  - `ConsoleUI`: Manages the ANSI-colored terminal aesthetics.

- **FIFO Conflict Resolution:** 
  For missions sharing the exact same priority score, the system automatically retrieves the `LocalDateTime` timestamp and applies strict *First-In-First-Out* protocol.

- **Memory Optimization:** 
  Safe code execution with robust Exception Handling. Dereferences extracted nodes (`heap[size - 1] = null;`) to assist the Java Garbage Collector.

---

## 🛠️ How It Works (The Math)

A standard Binary Heap assigns 2 children per node. This project utilizes a **Power of Two (`k`)** multiplier. 
If `k = 2`, the structure behaves as a **4-ary Heap** (each node has $2^2 = 4$ children). 
This reduces the overall height of the tree, allowing for faster traversal in specific memory-bound scenarios.

**Index calculations based on `k`:**
* **Parent:** `(index - 1) >> k`
* **First Child:** `(index << k) + 1`

---

## 🚀 How to Run Locally

1. **Clone the repository:**
   ```bash
   git clone https://github.com/TVOJE_IME/Power-Of-Two-Max-Heap.git
