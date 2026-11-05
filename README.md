# ImmersiveFirstPerson
Minecraft mod to render the player model in first person<br/>
Tested on NeoForge 1.21.1 only<br/>
Download: https://modrinth.com/mod/immersivefirstperson<br/>
<br/>
This is my first published mod after only ~1 month of modding experience, but I tried my absolute hardest to make it the best I can.<br/>
<br/>
## IMPORTANT UPDATE:<br/>
I've decided that I want to work on other projects and will likely not be working on this mod anymore, unless it is a very important bugfix or something. I don't plan to update it to new versions, or add support for other mods or mod loaders. However, the mod is (and has always been) MIT license, so do whatever you want with the source code. I know it ain't much, but thanks to the few hundred of you who downloaded and tried out my first mod. See you in the next project.<br/>
## Why does this mod exist when there is already a very popular, well-known first person mod?<br/>
  -Short answer: I wanted to improve some aspects of it for my own personal use, and use it as a learning experience.<br/>
  -Long answer: Some things really bugged me about that mod, mostly the camera position and the swimming. The swimming animation in this mod always displays the arms in front of you no matter your rotation. The camera is also highly customizable in all 3 directions (no 
  rotation, yet?). I just didn't like how your EYES would rotate, not your whole head, and it really ruined the immersion for me.<br/>
  With that being said, the other mod is made by a very talented creator and made much better than mine, so go check it out as well! (if you somehow haven't already)<br/>
<br/>
## Problems I'm aware of:<br/>
  -Fishing rod line does not render properly when riding certain entities (and boats when using Not Enough Animations).<br/>
  -Camera is still slightly buggy/jittery when colliding with blocks.<br/>
<br/>
## Does this mod support other popular mods, such as Not Enough Animations?<br/>
  I have not done extensive testing, but from my experience NEA does not seem to break anything other than the fishing line.<br/>
  As far as other mods go, anything that simply adds animations using the *vanilla* system should work fine. Mods that change the camera, or have their own player animation system may not work.



## TaCZ compatibility fork (Minecraft 1.21.1 / NeoForge)

This source fork adds a TaCZ-specific first-person rendering path:
- When the local player holds an item implementing TaCZ `IGun`, IFP does not cancel
  `ItemInHandRenderer.renderHandsWithItems`, allowing TaCZ's first-person renderer,
  weapon poses and animation injections to run.
- IFP's player-body render hides both body-model arms while a TaCZ gun is held, but
  leaves the torso, legs and other body parts visible.
- IFP does not replace TaCZ gun item rendering with its own static item-render path.

Build target: Minecraft 1.21.1, NeoForge 21.1.233, TaCZ NeoForge 1.1.8-hotfix-r7.
TaCZ is a required client dependency for this compatibility fork.

This is an initial source-level compatibility implementation. It still requires
in-game verification with the exact TaCZ port, especially draw/holster, ADS,
reload, inspect, offhand and non-gun item transitions.

## Follow-up fix: keep TaCZ's first-person gun view model

The IFP body render uses `PlayerRenderer`, whose `PlayerItemInHandLayer` also
renders held items using `THIRD_PERSON_*` display transforms. When that layer
renders a TaCZ gun during IFP's first-person body pass, it produces the low-detail
third-person gun model in the camera. `PlayerItemInHandLayerMixin` now cancels
only that local-player TaCZ gun draw while the camera is in first person. It does
not cancel the regular `ItemInHandRenderer` hand pass, so TaCZ remains responsible
for its animated first-person weapon and hands. Other held items and third-person
views are unaffected.

This source update has not been runtime-tested in Minecraft; verify draw/holster,
reload, inspect, ADS, firing, and offhand behavior in-game.

## Follow-up v3: preserve third-person EMF/ETF animation lifecycle

`PlayerRendererMixin` now replaces the local player's render path only while
in first-person camera mode. In third-person camera mode it no longer cancels
`PlayerRenderer` and manually calls `super.render`; vanilla's original
`PlayerRenderer` lifecycle is allowed to run. This is intended to avoid
interfering with EMF/ETF player-model animation hooks used by Fresh Animations:
Player Extension while TaCZ is held.

This is a targeted compatibility change, not a confirmed runtime fix. Test
third-person walking/sprinting with a TaCZ gun and Fresh Animations: Player
Extension enabled, then compare with the same setup with IFP disabled.

## Iris shader visibility issue: investigation still required

No Iris-specific renderer override is added in this source revision. Iris
explicitly documents that shader-pack core shaders replace/ignore custom
shader behavior supplied by mods, and that custom vertex formats/shaders do
not have a general automatic merge path. A safe TaCZ fallback must therefore
be applied at the actual TaCZ gun-model render-type/shader selection point;
blindly replacing buffers or render types in IFP could break gun textures,
attachments, scope stencil rendering, and shader-pack composition.

To implement that fallback accurately, capture the exact Iris version, shader
pack name/version, TaCZ jar build, and `latest.log` from a session where the
gun is invisible. Also test whether the gun becomes visible when shaders are
enabled but the shader pack is set to OFF. The current first-person TaCZ render
path is intentionally left unchanged in this revision.
