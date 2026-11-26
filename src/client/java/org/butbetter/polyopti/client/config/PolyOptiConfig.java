package org.butbetter.polyopti.client.config;

import org.butbetter.polyopti.client.PolyOptiRuntime;
import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.annotations.Dropdown;
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider;
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch;

public final class PolyOptiConfig extends Config {

    private static final String[] BLOCK_VISIBILITY_OPTIONS = {
            "hideTallGrass",
            "hideFlowers",
            "hideFerns",
            "hideFences",
            "hideGates",
            "hideHeads"
    };

    public static final PolyOptiConfig INSTANCE = new PolyOptiConfig();

    @Switch(title = "Enable PolyOpti", category = "General", description = "Master switch for every PolyOpti feature.")
    public boolean enabled = true;

    @Switch(title = "Low Graphics Mode", category = "General", description = "Applies a light graphics preset and keeps the heaviest visual effects disabled while it is on.")
    public boolean lowGraphicsMode = false;

    @Switch(title = "Limit Menu Frame Rate", category = "Frame Rate", description = "Caps the frame rate while any screen is open, including the pause menu, inventory and the title screen.")
    public boolean menuFpsEnabled = true;

    @Slider(title = "Menu Frame Rate", category = "Frame Rate", min = 5f, max = 240f, step = 1f, description = "Frame rate cap used while a screen is open.")
    public float menuFps = 30f;

    @Switch(title = "Limit Background Frame Rate", category = "Frame Rate", description = "Caps the frame rate while the game window is not focused.")
    public boolean backgroundFpsEnabled = true;

    @Slider(title = "Background Frame Rate", category = "Frame Rate", min = 1f, max = 60f, step = 1f, description = "Frame rate cap used while another window is focused.")
    public float backgroundFps = 5f;

    @Dropdown(title = "Culling Mode", category = "Culling", options = {"Off", "Balanced", "Performance", "Aggressive"}, description = "Balanced skips entities that are clearly outside the view cone, Performance adds occlusion raycasts, Aggressive runs the same checks with tighter distances and a larger budget.")
    public int cullMode = 2;

    @Switch(title = "Render Distance Limits", category = "Render Distance", description = "Enables the per-category render distance limits below.")
    public boolean distanceLimitsEnabled = true;

    @Slider(title = "Entity Distance", category = "Render Distance", min = 16f, max = 512f, step = 1f, description = "Upper limit for every entity, applied before the per-category limits.")
    public float entityDistance = 192f;

    @Slider(title = "Player Distance", category = "Render Distance", min = 16f, max = 512f, step = 1f, description = "Render distance for player models.")
    public float playerDistance = 64f;

    @Slider(title = "Passive Mob Distance", category = "Render Distance", min = 16f, max = 512f, step = 1f, description = "Render distance for animals and other peaceful mobs.")
    public float passiveDistance = 64f;

    @Slider(title = "Hostile Mob Distance", category = "Render Distance", min = 16f, max = 512f, step = 1f, description = "Render distance for hostile mobs.")
    public float hostileDistance = 64f;

    @Slider(title = "Other Entity Distance", category = "Render Distance", min = 16f, max = 512f, step = 1f, description = "Render distance for projectiles, boats, minecarts, items and everything else.")
    public float miscDistance = 64f;

    @Slider(title = "Nametag Distance", category = "Render Distance", min = 0f, max = 256f, step = 1f, description = "Render distance for nametags above players and entities. Zero keeps vanilla behaviour.")
    public float nametagDistance = 64f;

    @Slider(title = "Cosmetic Distance", category = "Render Distance", min = 0f, max = 256f, step = 1f, description = "Render distance for decorative layers such as capes and shoulder parrots. Zero keeps vanilla behaviour.")
    public float cosmeticDistance = 128f;

    @Slider(title = "Block Entity Distance", category = "Render Distance", min = 0f, max = 512f, step = 1f, description = "Render distance for chests, signs, banners, beacons and other block entities. Zero keeps vanilla behaviour.")
    public float blockEntityDistance = 64f;

    @Switch(title = "Simplify Distant Animations", category = "Render Distance", description = "Freezes secondary walk animations of entities that are far away.")
    public boolean simplifyDistantAnimations = false;

    @Slider(title = "Animation Simplify Distance", category = "Render Distance", min = 16f, max = 256f, step = 1f, description = "Entities beyond this distance stop updating secondary animations.")
    public float simplifyDistance = 96f;

    @Switch(title = "Reduce Particles", category = "Particles", description = "Spawns fewer particles during explosions, attacks and other dense effects.")
    public boolean reduceParticles = false;

    @Slider(title = "Particle Distance", category = "Particles", min = 0f, max = 128f, step = 1f, description = "Distance at which particles are no longer rendered. Zero keeps vanilla behaviour.")
    public float particleDistance = 32f;

    @Slider(title = "Particle Limit", category = "Particles", min = 200f, max = 20000f, step = 100f, description = "Hard cap for the amount of active particles in the world.")
    public float particleLimit = 4000f;

    @Switch(title = "Hide Lava Particles", category = "Particles", description = "Skips lava, dripping lava and landing lava particles.")
    public boolean hideLavaParticles = false;

    @Switch(title = "Hide Spawner Particles", category = "World", description = "Skips the flames and smoke emitted by monster spawners.")
    public boolean hideSpawnerParticles = false;

    @Switch(title = "Hide Tall Grass", category = "World", description = "Skips rendering short grass and tall grass.")
    public boolean hideTallGrass = false;

    @Switch(title = "Hide Flowers", category = "World", description = "Skips rendering flowers and flower beds.")
    public boolean hideFlowers = false;

    @Switch(title = "Hide Ferns And Bushes", category = "World", description = "Skips rendering ferns, dead bushes and sugar cane.")
    public boolean hideFerns = false;

    @Switch(title = "Hide Fences", category = "World", description = "Skips rendering fences.")
    public boolean hideFences = false;

    @Switch(title = "Hide Fence Gates", category = "World", description = "Skips rendering fence gates.")
    public boolean hideGates = false;

    @Switch(title = "Hide Heads", category = "World", description = "Skips rendering decorative skulls and heads.")
    public boolean hideHeads = false;

    @Switch(title = "Hide Armor Stands", category = "World", description = "Skips rendering armor stands, which is useful for decoration heavy servers.")
    public boolean hideArmorStands = false;

    @Switch(title = "Hide Item Frames", category = "World", description = "Skips rendering item frames.")
    public boolean hideItemFrames = false;

    @Switch(title = "Hide Glow Item Frames", category = "World", description = "Skips rendering glowing item frames while regular frames stay visible.")
    public boolean hideGlowItemFrames = false;

    @Switch(title = "Hide Maps In Frames", category = "World", description = "Skips rendering the map contents inside item frames.")
    public boolean hideFramedMaps = false;

    @Switch(title = "Hide Stuck Arrows", category = "World", description = "Skips the arrows and stingers embedded in players and mobs.")
    public boolean hideStuckArrows = false;

    @Switch(title = "Hide Ground Arrows", category = "World", description = "Skips arrows that are stuck in blocks.")
    public boolean hideGroundArrows = false;

    @Switch(title = "Hide Spawner Mob", category = "World", description = "Skips the rotating mob preview inside monster spawners.")
    public boolean hideSpawnerMob = false;

    @Switch(title = "Hide Sign Text", category = "World", description = "Skips the text of signs while the sign board itself stays visible.")
    public boolean hideSignText = false;

    @Switch(title = "Hide Beacon Beams", category = "World", description = "Skips the vertical beacon beams.")
    public boolean hideBeaconBeams = false;

    @Switch(title = "Hide World Border", category = "World", description = "Skips the world border wall and tint.")
    public boolean hideWorldBorder = false;

    @Switch(title = "Hide Enchantment Glint", category = "World", description = "Skips the enchantment glint on items, armor and entities.")
    public boolean hideEnchantmentGlint = false;

    @Switch(title = "Hide First Person Fire", category = "World", description = "Skips the fire overlay that covers the screen while the player is burning.")
    public boolean hideFirstPersonFire = false;

    @Switch(title = "Limit Sound Distance", category = "Audio", description = "Skips positional sounds that are further away than the configured distance.")
    public boolean limitSoundDistance = false;

    @Slider(title = "Sound Distance", category = "Audio", min = 8f, max = 128f, step = 1f, description = "Maximum distance for positional sounds.")
    public float soundDistance = 48f;

    private PolyOptiConfig() {
        super("polyopti.json", "assets/polyopti/polyopti.svg", "PolyOpti", Category.PERFORMANCE);

        addDependency("menuFps", "menuFpsEnabled");
        addDependency("backgroundFps", "backgroundFpsEnabled");
        addDependency("simplifyDistance", "simplifyDistantAnimations");
        addDependency("soundDistance", "limitSoundDistance");
        disableAllUnless("enabled");

        String[] blockOptions = BLOCK_VISIBILITY_OPTIONS;
        if (blockOptions != null) {
            for (String option : blockOptions) {
                registerCallback(option, PolyOptiRuntime::onBlockHidingChanged);
            }
        }

        registerCallback("enabled", PolyOptiRuntime::onSettingsChanged);
        registerCallback("lowGraphicsMode", PolyOptiRuntime::onSettingsChanged);
    }

    private void registerCallback(String option, Runnable callback) {
        if (option == null || option.isBlank() || callback == null) {
            return;
        }
        addCallback(option, callback);
    }
}
