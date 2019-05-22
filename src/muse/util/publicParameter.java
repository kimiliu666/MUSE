package muse.util;

public class publicParameter {

	public static final int averageSearchTime=3;

	public static final int ATT_SIZE=10;
	public static final String[] ATTRIBUTES={"short","handsome","male","good","clever","lovely","honest","nice","brave","trustful","rapture","generous","SH","teacher","north","hot",
							"romantic","hearty","reasonable","humor","diligent","evil","smart","married","old","poor","helpful","blinkered","cunning","faceless"};

	//5个关键字
//	public static final String[] KEYWORD_DICIONARY= {"cat","dog","boss","Maths","Chinese"};
	//10个
//	public static final String[] KEYWORD_DICIONARY= {"cat","dog","boss","Maths","Chinese","politic","USA","doctor","medicine","Christmas"};
	//30个


	public static final int KEYWORD_SIZE=10;
	public static final String[] KEYWORD_DICIONARY= {
			"cat","dog","boss","Maths","Chinese","politic","USA","doctor","medicine","Christmas",
			"Hollywood","music","money","pencil","rich","stock","salary","rice","televison","radio",
			"beautiful","food","drink","coffee","light","red","black","white","yellow","2019",
			"book","pencil","pen","ipad","smartphone","tower","father","nurse","today","magicbook",
			"cake","bed","boy","girl","ABC","MC","method","echo","wave","application",
			"way","control","blind","reduce","maintain","forward","aspect","sensor","Internet","abstract",
			"electronic","strength","detection","abort","measurement","compose","brave","afraid","enter","shift",
			"acraylic","art","glass","gifts","sheet","product","board","decoration","award","box",
			"transpareent","crafts","panels","wall","plant","electroplate","maker","mist","brochure","display",
			"menu","card","oil","habit","knowlege","desire","destiny","map","paradigm","behavior",
"admits",
"bombay",
"originals",
"enrichment",
"chennai",
"milford",
"buckle",
"bartlett",
"fetch",
"kitchens",
"ions",
"asshole",
"wat",
"rey",
"divers",
"faroe",
"townsend",
"blackburn",
"glendale",
"speedway",
"founders",
"sweatshirts",
"sundays",
"upside",
"admiral",
"yay",
"patron",
"sandwiches",
"sinclair",
"boiler",
"anticipate",
"activex",
"logon",
"induce",
"annapolis",
"padding",
"recruiter",
"popcorn",
"espanol",
"disadvantaged",
"trong",
"diagonal",
"unite",
"cracked",
"debtor",
"polk",
"mets",
"niue",
"ux",
"shear",
"mortal",
"sovereignty",
"supermarket",
"franchises",
"rams",
"cleansing",
"mfr",
"boo",
"hmmm",
"genomic",
"gown",
"helpdesk",
"ponds",
"archery",
"refuses",
"excludes",
"afb",
"sabbath",
"ruin",
"trump",
"nate",
"escaped",
"precursor",
"mates",
"adhd",
"avian",
"exe",
"stella",
"visas",
"matrices",
"anyways",
"xtreme",
"passages",
"etiology",
"vu",
"cereal",
"comprehension",
"tcl",
"sy",
"tow",
"resolving",
"mellon",
"drills",
"webmd",
"alexandra",
"champ",
"personalised",
"hospice",
"zerodegrees",
"agreeing",
"qos",
"exhibitor",
"rented",
"deductions",
"harrisburg",
"brushed",
"augmentation",
"otto",
"annuity",
"assortment",
"credible",
"sportswear",
"ik",
"cultured",
"importing",
"deliberately",
"recap",
"openly",
"toddlers",
"astro",
"crawl",
"chanel",
"theo",
"sparkling",
"jabber",
"hgh",
"bindings",
"hx",
"convincin",
	
	};


	static{
		if (ATTRIBUTES.length < publicParameter.ATT_SIZE) {
			System.err.println("属性集数量非法！");
		}
		if (KEYWORD_SIZE > publicParameter.KEYWORD_DICIONARY.length) {
			System.out.println("关键字范围过大！");

		}

	}

	
}
