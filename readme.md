
# 🔵 Treasure hunt — Java 2D Game  
*A simple top-down adventure game where you find three keys, unlock three doors, and claim the treasure!*  

This project is the **first part** of the Java 2D Game Dev series by **RyiSnow**.  
All **sprites, tiles, and music assets** belong to **RyiSnow** — this repo uses them only for learning purposes.

---

## 🎮 Gameplay
You play as the Blue Boy exploring a small dungeon-like map.  
Your goal is simple:
- Find **3 keys**
- Unlock **3 doors**
- Reach the **treasure** to finish the game!

Movement, collisions, UI, objects, sounds — all implemented from scratch in pure Java.

---

## 🧩 Project Structure
```

tresure_hunt_v0/
├── src/
│    ├── main/
│    ├── entity/
│    ├── tile/
│    └── object/
├── res/        # images / audio from RyiSnow tutorial
├── out/        # compiled classes (auto-generated)
├── .gitignore
└── README.md

```

---

## 🛠️ How to Compile & Run  
**Requires:** JDK 17+ recommended (works on 11+)

From the **project root**, run:

### 🔨 Compile
```bash
mkdir -p out
find src -name "*.java" > sources.txt
javac -d out @sources.txt
````

### ▶️ Run

```bash
java -cp out:res main.Main
```

> The `res` folder is added to the classpath so the game can load images/audio.

---

## 📚 Based On

This project is based on **RyiSnow’s Java 2D Game Tutorial Series** on YouTube.
All graphics, sound effects, and some map assets belong to **RyiSnow**.
Please support the original creator!

---

## 🚀 Future Improvements

* Add enemies
* Add more maps
* Add item inventory system
* Add animations for walking / picking items
* Save / load system

---

## 📜 License

This project is for **learning and personal use**.
Do not redistribute the original assets from RyiSnow commercially.

---

Enjoy hacking & improving this little adventure game! 😊

```
