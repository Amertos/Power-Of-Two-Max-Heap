# 🚀 Mission Dispatcher Pro (Generic Power-of-Two Max Heap)


🌟 Key Features
Generic Structure (<T extends Comparable<T>>): The algorithm is not tightly coupled with missions. It can process any class that implements the Comparable interface.

Mathematical Optimization: Leverages bitwise operations (1 << k, >> k) for rapid calculation of parent and child indices within the array, significantly accelerating CPU cycles.

S.O.L.I.D. Principles: Separates business logic (Dispatcher) from the data structure itself (Heap) and the presentation layer (ConsoleUI).

FIFO Conflict Resolution: For missions with identical priorities, the system automatically detects the creation LocalDateTime and applies the First-In-First-Out rule.

Clean Code & Exception Handling: Safe code, memory optimization for faster Garbage Collection, and proper handling of edge-case scenarios (e.g., extracting from an empty Heap).

💻 Technologies
Java (Standard Edition)

ANSI Terminal Colors (for a dynamic UI display)