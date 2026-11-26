# PolyOpti
![Modrinth Downloads](https://img.shields.io/modrinth/dt/8dK8MuZN)

Client-side performance mod for Minecraft with a OneConfig settings menu.

PolyOpti keeps the frame budget under control on weak hardware and in heavy scenes: it limits
frame rates, shortens render distances per entity category, culls invisible things, budgets
particles and lets you remove visual noise that costs frames but adds little gameplay value.

## Requirements

- Fabric Loader 0.20.1 or newer
- OneConfig 1.9.5 or newer

## Features

**Frame rate**
- Menu frame rate limit (any open screen, including inventory and the title screen).
- Background frame rate limit while the window is unfocused or minimised.

**Culling**
- Off / Balanced / Performance / Aggressive modes.
- Distance, behind-camera and raycast occlusion checks with a per-frame budget.
- Results are cached for several frames and dropped when the camera moves far enough.

**Render distance**
- Global entity limit plus separate limits for players, passive mobs, hostile mobs and other entities.
- Nametag, cosmetic layer and block entity distances.
- Optional simplification of secondary animations on distant entities.

**Particles and audio**
- Optional particle thinning, particle distance and a hard cap for active particles.
- Lava particle and monster spawner particle removal.
- Distance based culling of positional sounds.

**World**
- Hiding of tall grass, ferns, flowers, fences, fence gates, heads, armor stands, item frames,
  glowing item frames, maps inside frames, stuck and grounded arrows, spawner mob previews,
  sign text, beacon beams, the world border, enchantment glint and the first person fire overlay.

## Building

The build requires a Java 25 toolchain.

```shell
./gradlew build
```

The mod jar is placed in `build/libs`.

## Configuration

All options live in the OneConfig menu under the Performance category and are stored in
`config/polyopti.json`. Every option has a OneConfig reset control and the two frame rate
sliders have a fast reset because they are the most frequently adjusted values.

## Compatibility

PolyOpti uses only the modern render pipeline of Minecraft 26.3+: render states, feature
renderers and the level extractor. It never issues legacy fixed function OpenGL calls, so it
coexists with VulkanMod and other renderer replacements. 

Mixins are defensive: every hook is wrapped so that a failed or conflicting injection is logged
once and skipped instead of crashing the game.

## License

PolyOpti is licensed under LGPL-3.0-only. See `LICENSE`.
