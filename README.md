<div align="center" style="text-align: center;">

# Symbols'n'Signs
Collection of meaningful symbol items for Minecraft Java Edition and ways to display them.

![Symbols'n'Signs banner showing off all symbol items and the Display Panel block entities](./.github/assets/banner.png)

</div>

<br>

## Introduction:
Symbols'n'Signs is a Minecraft mod for NeoForge and Fabric that adds a collection of symbol items and road signs to the game.  
  
The symbols are designed to be meaningful and can be used for labeling purposes, like making Create's Redstone Link frequencies more recognizable.  
They are regular items that are cheaply craftable and transmutable and have uniform and recognizable textures.  
  
The signs are designed after European (and specifically German) road signs.  
There are many different ways to display these signs, including signal-carrying sign posts.  
  
**[View the latest documentation here (Note: contains changed and unreleased content).](https://github.com/the-drunken-cod/Symbols-n-Signs/blob/develop/docs.md)**

<br>

## Installation:
You can visit the [releases page](https://github.com/the-drunken-cod/Symbols-n-Signs/releases), the [Modrinth page](https://modrinth.com/mod/factory-symbols), or the [CurseForge page](https://www.curseforge.com/minecraft/mc-mods/factory-symbols) to download the latest version of Symbols'n'Signs.  
Then simply place the downloaded JAR file into your Minecraft `mods` folder and launch the game with either NeoForge or Fabric.  
For multiplayer, ensure the mod is installed on the server and all clients connecting to it. [Automodpack](https://modrinth.com/mod/automodpack) can make this process easier.
  
> [!IMPORTANT]  
> The **Fabric version** requires [Cloth Config API](https://modrinth.com/mod/cloth-config) to be installed as well.

> [!NOTE]  
> You will need either the [NeoForge](https://neoforge.dev/) or [Fabric](https://fabricmc.net/) mod loader installed to run the mod.  
> An easy way of doing this is by creating a modded game instance in a launcher like [ATLauncher](https://atlauncher.com/) or [MultiMC.](https://multimc.org/)

<br>

## Development Notes:
- This mod is still in early development, so expect major changes and breaking updates until the v1.0.0 release.
- Developer docs can be found in the [`dev_docs.md` file.](./dev_docs.md) They will contain more technical information.
- DataGen JSONs (in `neoforge/src/generated` and `fabric/src/generated`) will be excluded from the repository until the v1.0.0 release. If you still want access to those files, you can either unzip the released JAR file, or set up the Java dev env and run the `./gradlew :neoforge:runData` or `./gradlew :fabric:runData` command.
- If you want to contribute to the project, firstly thank you :), and secondly, please refer to the [`CONTRIBUTING.md`](./CONTRIBUTING.md) file for guidelines and instructions.

<br>

## Compatibility:
- **Sable / Create Aeronautics:**
  - Block weight support

<br>

## Attribution:
- Created using [a modified version](https://github.com/the-drunken-cod/MultiLoader-Template) of [jaredlll08/MultiLoader-Template](https://github.com/jaredlll08/MultiLoader-Template)
- Sound effects:
  - [`ratchet.wav` by caseymoura](https://freesound.org/s/445492/) - License: Creative Commons Attribution 3.0
  - [`ratchet socket wrench tool` by AlaskaRobotics](https://freesound.org/s/551497/) - License: Creative Commons 0
  - [`Tools Ratchet.wav` by CapsLok](https://freesound.org/s/181634/) - License: Creative Commons 0
  - [`Paper Rustling 01.wav` by swidmark](https://freesound.org/s/171320/) - License: Creative Commons 0
  - [`Paper Rustling 02.wav` by swidmark](https://freesound.org/s/171325/) - License: Creative Commons 0
  - [`Tearing Paper 4.wav` by F.M.Audio](https://freesound.org/s/557138/) - License: Creative Commons Attribution 4.0
  - [`Pen Write Pad_Pen Scribble Pad.wav` by clgood](https://freesound.org/s/688618/) - License: Creative Commons 0
  - [`OBJWrite_Pen Writing Paper Soft Close_CP_NONE` by kzarkses](https://freesound.org/s/746916/) - License: Creative Commons Attribution 4.0
- Models made with [Blockbench](https://blockbench.net/)
- Textures made with [Paint.NET](https://paint.net/)
- Recipe screenshots taken with [EMI](https://modrinth.com/mod/emi)
- Symbols inspired by the [virtual circuit network symbols](https://wiki.factorio.com/Circuit_network#Virtual_signals) in the game [Factorio](https://www.factorio.com/)
- Signs inspired by the European, and specifically German StVO road signs, including an improvised pixelated version of the [DIN 1451](https://en.wikipedia.org/wiki/DIN_1451) font.

<br>

## Modpack Policy:
You are free to use Symbols'n'Signs in any modpacks; public or private :)  
Just make sure you abide by [our licenses](#licenses), common sense, and [the Minecraft EULA.](https://minecraft.net/en-us/eula)  
We would also appreciate a mention in the credits section of your modpack and a quick shout on our [discussion board](https://github.com/the-drunken-cod/Symbols-n-Signs/discussions) about your modpack (so we can check it out and play it ourselves!)  
Please also consider [reporting any issues or suggestions](https://github.com/the-drunken-cod/Symbols-n-Signs/issues), so we can improve the mod for you and other players and have better compatibility with other mods.

<br>

## Licenses:
Code is licensed under the [AGPL-3.0-only.](./LICENSE.txt)  
Original resources and assets in `common/src/main/resources/` are licensed under [MIT](src/main/resources/LICENSE.txt) unless otherwise stated.

<br>

## AI Usage:
We use GenAI to make tedious work easier and bridge shortcomings. [Read our full policy here.](https://github.com/the-drunken-cod#genai-usage)  
That being said, every generated line is reviewed, and all assets remain fully human-made.

<br>

## Disclaimers:
NOT AN OFFICIAL MINECRAFT PRODUCT. NOT APPROVED BY OR ASSOCIATED WITH MOJANG OR MICROSOFT.  
  
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
