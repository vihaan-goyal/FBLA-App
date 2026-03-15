# 🐾 PetLife

## 📖 Overview

**PetLife** is a Java-based interactive program that simulates caring for a digital pet while managing the financial responsibilities of pet ownership.

The game combines a **2D exploration environment** with a **virtual pet care system** where players complete tasks, earn money, purchase supplies, and improve their pet’s wellbeing.

The goal of the program is to demonstrate the **responsibility and cost of owning a pet** through interactive gameplay.

The program was built using **Java and Java Swing**.

---

# ▶️ How to Run the Program

## 🧰 Requirements

- Java **JDK 8 or higher**
- A Java IDE  
  - VS Code  
  - IntelliJ  
  - Eclipse  

---

## 🚀 Running the Program

1. Download or clone the repository.
2. Open the project in your Java IDE.
3. Navigate to `Main.java`.
4. Run the program.

The game window will launch and the player can begin interacting with the world.

The program begins execution from the `main()` method in the `Main` class.

---

# 🎮 Controls

| Key | Action |
|----|------|
| **W** | Move Up |
| **A** | Move Left |
| **S** | Move Down |
| **D** | Move Right |

### 🐶 Pet Care Actions

| Key | Action |
|----|------|
| **F** | Feed Pet |
| **M** | Medicate Pet |
| **P** | Play with Pet |

### 📊 Game Interface

| Key | Menu |
|----|------|
| **1** | Wallet |
| **2** | Inventory |
| **3** | TODO List |
| **4** | Pet Stats |

Keyboard input is processed through the program’s input handler which listens for key events.

---

# ⭐ Game Features

## 🐕 Virtual Pet System

Players create and care for a virtual pet such as:

- 🐶 Dog  
- 🐱 Cat  
- 🐨 Koala  

Each pet has attributes that respond to how well it is treated.

The pet system is managed through the **PetManager**, which tracks and creates pets.

---

## ✅ Tasks and Rewards

Players complete tasks such as visiting locations in the world.

Completing tasks rewards the player with:

- 💰 Money
- 😊 Increased pet happiness

Tasks are managed using the **TaskManager** and **Task** classes.

---

## 💵 Financial Responsibility System

Players must manage money to care for their pet.

Examples include:

- Paying for supplies and provisions
- Completing tasks to earn rewards

This system demonstrates the **financial responsibility of pet ownership**.

---

## 🧑‍🤝‍🧑 NPC Interactions

The game includes several interactive NPC characters:

- 🧙 **Wiseman NPC** – gives quests and rewards  
- 🛒 **Merchant NPCs** – sell food, medicine, and toys  
- 🧠 **Quiz Master NPC** – provides educational quizzes graded using AI  

These interactions guide players through the gameplay and encourage responsible pet care.

---

## 🌍 Exploration-Based Gameplay

The game uses a **tile-based world** where the player can explore and interact with objects such as:

- Treasure chests
- Pet bed
- Decorative objects

Collision detection prevents players from moving through walls or obstacles.

---

# 🏗 Program Structure

The program is organized into several packages to maintain a modular code structure.

### 📂 `main`
Handles the **game loop, UI, sound system, and window setup**

### 📂 `entity`
Contains **player and NPC classes**

### 📂 `object`
Contains interactable world objects  
(keys, doors, boots, chests)

### 📂 `tile`
Handles **map loading and rendering**

### 📂 `finance`
Handles **transactions and wallet**

### 📂 `inventory`
Handles **item count and manipulation of them**

### 📂 `quest`
Handles **tasks and objectives**

### 📂 `pet`
Manages the **virtual pet system**

### 📂 `quiz`
Manages the **specific Quizmaster using ChatGPT API to grade responses**

This modular structure improves **code readability, maintainability, and organization**.

---

# 🧠 Data Structures and Logic

The program uses several data structures to manage game information.

Examples include:

- **Arrays** for storing game objects and world data
- **ArrayLists** for tracking tasks
- **Classes and objects** for modular design
- **Conditional logic** for gameplay events

---

# ✔️ Input Validation

The program includes input validation to maintain correct gameplay behavior.

Examples include:

- Preventing the player from walking through walls
- Checking if the player has enough money before making purchases
- Verifying that tasks are completed before giving rewards

---

# 🛠 Libraries and Technologies Used

- **Java**
- **Java Swing** – GUI and rendering
- **Java AWT** – keyboard input

No external third-party libraries were required.

---

# 📚 External Resources and Credits

Graphics are used for **educational purposes**.

Programming concepts were inspired by common **Java game development tutorials**.

All original game logic and systems were implemented by the development team.

---

# 👨‍💻 Authors

Developed by the **FBLA Introduction to Programming Team**

- **Vihaan Goyal**
- **Logan Russon**
- **Lev Rubin**

📍 Westhill High School
