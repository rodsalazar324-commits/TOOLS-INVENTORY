
package com.example;

public class ToolCatalog {

    public static void loadTools() {

        String[][] catalog = {

            {
                "HT", "Hand Tool",
                "Hammer|Screwdriver|Pliers|Adjustable Wrench|Combination Wrench|Socket Wrench|Allen Key|Chisel|Handsaw|Pipe Wrench|Wire Stripper|Utility Knife|Crowbar|Bolt Cutter|Ratchet"
            },

            {
                "PT", "Power Tool",
                "Electric Drill|Cordless Drill|Angle Grinder|Circular Saw|Jigsaw|Reciprocating Saw|Power Sander|Impact Driver|Impact Wrench|Rotary Tool|Heat Gun|Electric Planer|Electric Screwdriver|Power Cutter|Welding Machine"
            },

            {
                "MT", "Measuring Tool",
                "Measuring Tape|Steel Ruler|Meter Stick|Spirit Level|Digital Caliper|Vernier Caliper|Micrometer|Laser Distance Meter|Protractor|Combination Square|Try Square|Angle Finder|Feeler Gauge|Measuring Wheel|Plumb Bob"
            },

            {
                "GT", "Gardening Tool",
                "Shovel|Spade|Garden Rake|Hoe|Garden Trowel|Pruning Shears|Hedge Shears|Garden Fork|Hand Cultivator|Leaf Blower|Lawn Mower|Grass Trimmer|Axe|Pickaxe|Wheelbarrow"
            },

            {
                "SE", "Safety Equipment",
                "Safety Helmet|Safety Goggles|Face Shield|Ear Protection|Dust Mask|Respirator|Work Gloves|Cut-Resistant Gloves|Safety Vest|Safety Harness|Steel-Toe Boots|Knee Pads|Welding Helmet|Welding Gloves|Protective Apron"
            },

            {
                "WT", "Welding Tool",
                "Welding Machine|Welding Clamp|Welding Torch|Welding Chipping Hammer|Wire Brush|Metal File|Bench Vise|C-Clamp|Metal Shears|Plasma Cutter|Soldering Iron|Soldering Gun|Deburring Tool|Metal Punch|Pipe Cutter"
            },

            {
                "WW", "Woodworking Tool",
                "Wood Chisel|Hand Plane|Coping Saw|Wood Rasp|Wood File|Claw Hammer|Mallet|Circular Saw|Jigsaw|Router|Wood Clamp|Nail Set|Miter Saw|Table Saw|Wood Carving Knife"
            }

        };

        int added = 0;

        for (String[] category : catalog) {

            String prefix = category[0];
            String categoryName = category[1];

            String[] tools = category[2].split("\\|");

            for (int i = 0; i < tools.length; i++) {

                String id = prefix + String.format("%03d", i + 1);

                Tool tool = new SeedTool(
                    id,
                    tools[i],
                    "Good",
                    "Available",
                    "Tool Room A",
                    "Initial catalog item",
                    categoryName
                );

                if (ToolStore.addTool(tool)) {
                    added++;
                }
            }
        }

        System.out.println(
            "Tool catalog loaded. New tools added: " + added
        );
    }
}

// Tool class used for catalog entries
class SeedTool extends Tool {

    private String category;

    public SeedTool(
        String id,
        String name,
        String condition,
        String status,
        String location,
        String description,
        String category
    ) {

        super(
            id,
            name,
            condition,
            status,
            location,
            description
        );

        this.category = category;
    }

    @Override
    public String getCategory() {
        return category;
    }
}
