# SimpleRTP

SimpleRTP is a simple random teleport plugin. It is designed to be lightweight and easy to use.

## Commands
| Command | Description | Default Permission |
| ------- | ----------- |--------------------|
| /rtp | Teleport to a random location | rtp.use            |

## Config
Everything is configurable in the config.yml file. The default config is as follows:
```yaml
world: "world"

cooldown:
  rtp: 60

permissions:
  rtp: "rtp.use"
  cooldownBypass: "rtp.cooldown.bypass"

messages:
  no-permission: "&cYou do not have permission to use this command."
  teleported: "&aYou have been teleported to a location {x}, {z}!"
  waiting: "&cPlease wait while we find a location for you to teleport to."
  cooldown: "&aYou must wait &c{time} &aseconds before using this command again."

border:
  x-min: -1000
  x-max: 1000
  z-min: -1000
  z-max: 1000
```