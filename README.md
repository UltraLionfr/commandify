# 🧭 Commandify

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-blue)
![Loader](https://img.shields.io/badge/NeoForge-21.1.208%2B-green)
![License](https://img.shields.io/badge/License-All%20Rights%20Reserved-red)
![Dependencies](https://img.shields.io/badge/Dependencies-None-lightgrey)

**Commandify** is a lightweight, modular, and server-side **NeoForge mod** that brings back all the essential commands every Minecraft server deserves — from teleportation and homes to world management, chat tools, and admin utilities.  
Perfect for servers that want **clean, functional commands without unnecessary bloat.**

---

## ✨ Features

- ✅ Modular system — commands can be enabled or disabled in the config  
- 🌐 Multi-language support (`en_us`, `fr_fr`)  
- 💾 Automatic config & language file generation  
- ⚙️ Simple TOML-based configuration (`/config/commandify/`)  
- 🧭 Complete teleportation system: homes, warps, spawn, TPA, back, RTP  
- 🌤️ World time & weather management  
- 💬 Chat utilities (private messages, broadcast, ignore list)  
- 🧰 Admin tools (inventory viewing)  
- 🔒 Server-side only — **no client installation required**  
- 🚫 No external dependencies  

---

## ⚙️ Configuration

After first launch, all config files are generated under:
```

/config/commandify/

```

- `commandify-general.toml` — global settings  
- `teleportation.toml` — teleportation commands  
- `world.toml` — world/time/weather management  
- `chat.toml` — chat system  
- `admin.toml` — admin & moderation tools  
- `lang/` — contains `en_us.json` and `fr_fr.json` for localization  

---

## 💬 Commands

### 🧭 Teleportation Commands
| Command | Description |
|----------|-------------|
| `/home [name]` | Teleports you to your set home location. |
| `/sethome [name]` | Sets a home location with an optional name. |
| `/delhome [name]` | Deletes a specified home location. |
| `/spawn` | Teleports you to the server’s spawn point. |
| `/setspawn` | Sets the server’s spawn point to your current location. |
| `/warp [name]` | Teleports you to a pre-set warp point. |
| `/setwarp [name]` | Creates a warp point at your current location. |
| `/delwarp [name]` | Deletes a specified warp point. |
| `/tpa [player]` | Sends a teleport request to another player. |
| `/tpahere [player]` | Requests another player to teleport to you. |
| `/tpaccept` | Accepts a pending teleport request. |
| `/tpdeny` | Denies a pending teleport request. |
| `/tpatoggle` | Toggles whether other players can send you teleport requests. |
| `/back` | Teleports you to your last location before death or teleportation. |
| `/rtp` | Randomly teleports you within a configured radius. |

---

### ☀️ World Management Commands *(OP only)*
| Command | Description |
|----------|-------------|
| `/day` | Sets time to day. |
| `/night` | Sets time to night. |
| `/noon` | Sets time to noon. |
| `/midnight` | Sets time to midnight. |
| `/sun` | Sets weather to clear. |
| `/rain` | Sets weather to rain. |
| `/thunder` | Sets weather to thunderstorms. |

---

### 💬 Chat Commands
| Command | Description |
|----------|-------------|
| `/msg [player] [message]` | Sends a private message to a player. |
| `/r [message]` | Replies to the last private message. |
| `/broadcast [message]` | Sends a highlighted message to all players. |
| `/ignore [player]` | Blocks private messages from a player. |
| `/unignore [player]` | Unblocks a player. |
| `/ignored` | Lists all ignored players. |

---

### 🛡️ Admin & Utility Commands
| Command | Description |
|----------|-------------|
| `/invsee [player]` | View and modify another player's inventory. |
| `/commandify info` | Shows mod info, author, and version. |
| `/commandify reload` | Reloads all Commandify configs. |

---

## 🔧 Installation

1. Download the latest **Commandify** `.jar`
2. Place it in your server’s `mods/` folder
3. Start your server once to generate configs and language files
4. Edit your configuration in `/config/commandify/` if needed
5. Restart your server — done!

---

## 🧩 Compatibility

| Requirement | Version |
|--------------|----------|
| **Minecraft** | 1.21.1 |
| **Loader** | NeoForge **21.1.208 or newer** |
| **Environment** | Server-side only |
| **Dependencies** | None |

---

## 📁 Config File Structure

```

/config/commandify/
├── commandify-general.toml
├── teleportation.toml
├── world.toml
├── chat.toml
├── admin.toml
└── lang/
├── en_us.json
└── fr_fr.json

```

---

## 🧱 Planned Features
- Optional permission integration

---

## 🧑‍💻 Author

**Commandify** was created by **UltraLion**  
🌐 [https://ultralion.xyz](https://ultralion.xyz)

---

## 📜 License

This mod is distributed under the **All Rights Reserved** license.  
Redistribution or modification without explicit permission from the author is prohibited.
