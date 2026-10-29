package com.example.data.model

data class ProductUnitOption(
    val labelUrdu: String,
    val multiplier: Double, // price multiplier relative to base price
    val weightDescription: String
)

data class Product(
    val id: String,
    val nameUrdu: String,
    val nameEnglish: String,
    val categoryId: String,
    val descriptionUrdu: String,
    val basePrice: Double, // in PKR
    val defaultUnit: String,
    val unitOptions: List<ProductUnitOption>,
    val originOrGuarantee: String = "100% خالص، بغیر ملاوٹ، لودھراں کوالٹی گارنٹی",
    val isFeatured: Boolean = false,
    val isRationPackage: Boolean = false,
    val isDemoItem: Boolean = true
) {
    fun getPriceForUnit(unitLabel: String): Double {
        val option = unitOptions.firstOrNull { it.labelUrdu == unitLabel }
        return if (option != null) (basePrice * option.multiplier) else basePrice
    }
}

object SampleProducts {
    val PRODUCTS = listOf(
        // آٹا اور اناج (Atta & Flour)
        Product(
            id = "prod_atta_chakki",
            nameUrdu = "چکی کا خالص دیسی آٹا",
            nameEnglish = "Whole Wheat Chakki Atta",
            categoryId = "cat_flour",
            descriptionUrdu = "لودھراں کی منتخب شدہ پنجاب گندم سے پتھر کی چکی پر پسا ہوا 100% خالص آٹا۔ بغیر کسی کیمیکل یا ملاوٹ کے، قدرتی فائبر سے بھرپور۔ روٹی نرم اور لذیذ بنتی ہے۔",
            basePrice = 1450.0,
            defaultUnit = "10 کلوگرام",
            unitOptions = listOf(
                ProductUnitOption("5 کلوگرام", 0.52, "5 کلو پیکنگ"),
                ProductUnitOption("10 کلوگرام", 1.0, "10 کلوگرام تھیلا"),
                ProductUnitOption("20 کلوگرام", 1.95, "20 کلوگرام فیملی بوری")
            ),
            originOrGuarantee = "لودھراں مقامی کاشت - پتھر کی روایتی چکی",
            isFeatured = true
        ),
        Product(
            id = "prod_maida_fine",
            nameUrdu = "سپر فائن میدہ",
            nameEnglish = "Super Fine Maida",
            categoryId = "cat_flour",
            descriptionUrdu = "بیکنگ، سموسہ، نان اور پراٹھا کیلئے بہترین ڈبل چھنا ہوا سفید میدہ۔",
            basePrice = 160.0,
            defaultUnit = "1 کلوگرام",
            unitOptions = listOf(
                ProductUnitOption("1 کلوگرام", 1.0, "1 کلو پیکٹ"),
                ProductUnitOption("5 کلوگرام", 4.8, "5 کلو پیکنگ")
            ),
            originOrGuarantee = "پریمیئم فلور ملز کوالٹی"
        ),
        Product(
            id = "prod_besan",
            nameUrdu = "خالص چنا بیسن (چکی پسا)",
            nameEnglish = "Pure Chana Besan",
            categoryId = "cat_flour",
            descriptionUrdu = "100% خالص چنے کی دال سے تیار شدہ بیسن۔ پکوڑوں، کڑھی اور حلوہ جات کیلئے خوشبودار اور خالص۔",
            basePrice = 290.0,
            defaultUnit = "1 کلوگرام",
            unitOptions = listOf(
                ProductUnitOption("1 کلوگرام", 1.0, "1 کلو پاؤچ"),
                ProductUnitOption("2 کلوگرام", 1.95, "2 کلو پیکٹ")
            ),
            originOrGuarantee = "بغیر کسی ملاوٹ کے خالص چنا"
        ),

        // گھی اور کوکنگ آئل (Ghee & Cooking Oil)
        Product(
            id = "prod_desi_ghee",
            nameUrdu = "خالص دیسی گھی (گائے و بھینس)",
            nameEnglish = "Pure Desi Ghee",
            categoryId = "cat_oil_ghee",
            descriptionUrdu = "دیہات لودھراں کے دیسی مکھن سے کڑھا ہوا روایتی دیسی گھی۔ دانے دار، خوشبودار اور مکمل طور پر خالص۔ بچوں اور بزرگوں کی صحت کیلئے اکسیر۔",
            basePrice = 2400.0,
            defaultUnit = "1 کلوگرام",
            unitOptions = listOf(
                ProductUnitOption("500 گرام", 0.52, "نصف کلو جار"),
                ProductUnitOption("1 کلوگرام", 1.0, "1 کلوگرام شیشہ جار"),
                ProductUnitOption("2 کلوگرام", 1.95, "2 کلوگرام فیملی پیک")
            ),
            originOrGuarantee = "لودھراں خالص دیسی ڈیری فارم",
            isFeatured = true
        ),
        Product(
            id = "prod_cooking_oil",
            nameUrdu = "پریمیئم ویجیٹیبل کوکنگ آئل",
            nameEnglish = "Premium Cooking Oil",
            categoryId = "cat_oil_ghee",
            descriptionUrdu = "وٹامن A اور D سے بھرپور ہلکا اور صحت بخش کوکنگ آئل۔ ہاضمے کیلئے بہترین اور روزمرہ سالن کیلئے موزوں۔",
            basePrice = 520.0,
            defaultUnit = "1 لیٹر",
            unitOptions = listOf(
                ProductUnitOption("1 لیٹر", 1.0, "1 لیٹر پاؤچ"),
                ProductUnitOption("5 لیٹر کین", 4.9, "5 لیٹر ہینڈل کین")
            ),
            originOrGuarantee = "PSQCA تصدیق شدہ گریڈ A"
        ),
        Product(
            id = "prod_sarson_oil",
            nameUrdu = "کوہلو کا خالص سرسوں کا تیل",
            nameEnglish = "Pure Mustard Oil (Sarson)",
            categoryId = "cat_oil_ghee",
            descriptionUrdu = "لودھراں کے مقامی سرسوں کے بیجوں سے کولڈ پریسڈ (کولہو) کا نکلا ہوا خالص کڑوا تیل۔ اچار، ساگ اور سالن کیلئے بے مثال۔",
            basePrice = 580.0,
            defaultUnit = "1 لیٹر",
            unitOptions = listOf(
                ProductUnitOption("1 لیٹر", 1.0, "1 لیٹر بوتل"),
                ProductUnitOption("2 لیٹر", 1.95, "2 لیٹر کین")
            ),
            originOrGuarantee = "روایتی لکڑی کے کولہو کا تیل"
        ),

        // چاول (Rice)
        Product(
            id = "prod_basmati_super",
            nameUrdu = "سپر کرنل باسمتی چاول (پرانا چاول)",
            nameEnglish = "Super Kernel Basmati Rice",
            categoryId = "cat_rice",
            descriptionUrdu = "پنجاب کے بہترین کھیتوں کا سال پرانا خوشبودار باسمتی چاول۔ پکنے کے بعد لمبا دانہ اور دلفریب خوشبو۔ پلاؤ اور بریانی کیلئے لاجواب۔",
            basePrice = 360.0,
            defaultUnit = "1 کلوگرام",
            unitOptions = listOf(
                ProductUnitOption("1 کلوگرام", 1.0, "1 کلو پیک"),
                ProductUnitOption("5 کلوگرام", 4.85, "5 کلوگرام تھیلا"),
                ProductUnitOption("10 کلوگرام", 9.6, "10 کلوگرام بوری")
            ),
            originOrGuarantee = "دو سال پرانا خشک چاول",
            isFeatured = true
        ),
        Product(
            id = "prod_sella_rice",
            nameUrdu = "سپر سیلا کائنات چاول",
            nameEnglish = "Super Kainat Sella Rice",
            categoryId = "cat_rice",
            descriptionUrdu = "دیگی بریانی اور زردہ کیلئے مخصوص لمبا دانہ سیلا چاول۔ کھل کھلا دانہ جو ٹوٹتا نہیں۔",
            basePrice = 320.0,
            defaultUnit = "1 کلوگرام",
            unitOptions = listOf(
                ProductUnitOption("1 کلوگرام", 1.0, "1 کلو پیک"),
                ProductUnitOption("5 کلوگرام", 4.9, "5 کلو تھیلا")
            ),
            originOrGuarantee = "غلہ منڈی کوالٹی گریڈ 1"
        ),

        // دالیں اور چنے (Pulses)
        Product(
            id = "prod_dal_chana",
            nameUrdu = "دال چنا سپیشل (دھلی و صاف)",
            nameEnglish = "Dal Chana Special",
            categoryId = "cat_pulses",
            descriptionUrdu = "بڑے دانے والی صاف ستھری دال چنا۔ پتھر اور مٹی سے بالکل پاک۔ جھٹ پٹ گلنے والی۔",
            basePrice = 260.0,
            defaultUnit = "1 کلوگرام",
            unitOptions = listOf(
                ProductUnitOption("500 گرام", 0.52, "آدھا کلو پیکٹ"),
                ProductUnitOption("1 کلوگرام", 1.0, "1 کلو پیکٹ"),
                ProductUnitOption("2 کلوگرام", 1.95, "2 کلو پیکٹ")
            ),
            originOrGuarantee = "مشین کلین لودھراں پراسیسنگ"
        ),
        Product(
            id = "prod_dal_moong",
            nameUrdu = "دال مونگ دھلی ہوئی (پیلے دانے)",
            nameEnglish = "Dal Moong Washed",
            categoryId = "cat_pulses",
            descriptionUrdu = "خالص اور ہلکی پھلکی دال مونگ۔ فوری ہضم اور غذائیت سے بھرپور۔",
            basePrice = 280.0,
            defaultUnit = "1 کلوگرام",
            unitOptions = listOf(
                ProductUnitOption("500 گرام", 0.52, "آدھا کلو"),
                ProductUnitOption("1 کلوگرام", 1.0, "1 کلو پیکٹ")
            ),
            originOrGuarantee = "100% بغیر پالش خالص"
        ),
        Product(
            id = "prod_dal_mash",
            nameUrdu = "دال ماش دھلی ہوئی (سفید)",
            nameEnglish = "Dal Mash Washed",
            categoryId = "cat_pulses",
            descriptionUrdu = "ڈھابہ سٹائل کڑاہی دال ماش کیلئے فرسٹ کلاس کوالٹی۔ ہر دانہ الگ اور ذائقے دار۔",
            basePrice = 420.0,
            defaultUnit = "1 کلوگرام",
            unitOptions = listOf(
                ProductUnitOption("500 گرام", 0.52, "آدھا کلو"),
                ProductUnitOption("1 کلوگرام", 1.0, "1 کلو پیکٹ")
            ),
            originOrGuarantee = "اعلیٰ معیار کی لودھراں دال"
        ),
        Product(
            id = "prod_dal_masoor",
            nameUrdu = "دال مسور باریک لال",
            nameEnglish = "Red Lentils (Dal Masoor)",
            categoryId = "cat_pulses",
            descriptionUrdu = "چاولوں کے ساتھ تڑکا دال کیلئے پسندیدہ ترین لال مسور دال۔ صاف شدہ۔",
            basePrice = 290.0,
            defaultUnit = "1 کلوگرام",
            unitOptions = listOf(
                ProductUnitOption("500 گرام", 0.52, "آدھا کلو"),
                ProductUnitOption("1 کلوگرام", 1.0, "1 کلو پیکٹ")
            ),
            originOrGuarantee = "مکمل چھنی ہوئی گریڈ A"
        ),
        Product(
            id = "prod_chana_safaid",
            nameUrdu = "کابلی سفید چنے (موٹا دانہ)",
            nameEnglish = "Kabuli White Chana",
            categoryId = "cat_pulses",
            descriptionUrdu = "لاہوری نان چنے اور چنا چاٹ کیلئے موٹا ملائم سفید چنا۔",
            basePrice = 340.0,
            defaultUnit = "1 کلوگرام",
            unitOptions = listOf(
                ProductUnitOption("1 کلوگرام", 1.0, "1 کلو پیکٹ"),
                ProductUnitOption("2 کلوگرام", 1.95, "2 کلو پیکٹ")
            ),
            originOrGuarantee = "بڑے دانے کا درآمدی معیار"
        ),

        // چینی، چائے اور نمک (Sugar, Tea & Salt)
        Product(
            id = "prod_sugar_fine",
            nameUrdu = "سفید دانے دار چینی",
            nameEnglish = "Refined White Sugar",
            categoryId = "cat_sugar_tea",
            descriptionUrdu = "چمکدار سفید صاف ستھری دانے دار چینی۔ روزمرہ چائے اور میٹھے پکوانوں کیلئے۔",
            basePrice = 145.0,
            defaultUnit = "1 کلوگرام",
            unitOptions = listOf(
                ProductUnitOption("1 کلوگرام", 1.0, "1 کلو پیکٹ"),
                ProductUnitOption("5 کلوگرام", 4.95, "5 کلو فیملی تھیلا")
            ),
            originOrGuarantee = "پنجاب شوگر ملز سپلائی"
        ),
        Product(
            id = "prod_tea_blend",
            nameUrdu = "پریمیئم کینیا کڑک چائے",
            nameEnglish = "Premium Black Tea Leaf",
            categoryId = "cat_sugar_tea",
            descriptionUrdu = "شاندار رنگ اور کڑک خوشبو والی خالص کینیا چائے کی پتی۔ لودھراں کی روایتی دودھ پتی کیلئے لاجواب۔",
            basePrice = 580.0,
            defaultUnit = "450 گرام",
            unitOptions = listOf(
                ProductUnitOption("450 گرام", 1.0, "450 گرام فیملی پیک"),
                ProductUnitOption("900 گرام", 1.95, "900 گرام جیمبو پیک")
            ),
            originOrGuarantee = "خالص کینیا امپورٹڈ پتی",
            isFeatured = true
        ),
        Product(
            id = "prod_salt_lahori",
            nameUrdu = "لاہوری خالص آیوڈائزڈ نمک",
            nameEnglish = "Iodized Himalayan Pink Salt",
            categoryId = "cat_sugar_tea",
            descriptionUrdu = "کھیوڑہ کی کانوں سے نکلا ہوا قدرتی آیوڈین سے بھرپور باریک نمک۔ صحت بخش اور خالص۔",
            basePrice = 60.0,
            defaultUnit = "800 گرام",
            unitOptions = listOf(
                ProductUnitOption("800 گرام", 1.0, "800 گرام پیکٹ"),
                ProductUnitOption("3 پیکٹ بنڈل", 2.8, "تین پیکٹوں کا بنڈل")
            ),
            originOrGuarantee = "کھیوڑہ قدرتی نمک"
        ),

        // مصالحہ جات (Spices)
        Product(
            id = "prod_haldi_powder",
            nameUrdu = "خالص پیسی ہوئی دیسی ہلدی",
            nameEnglish = "Pure Turmeric Powder (Haldi)",
            categoryId = "cat_spices",
            descriptionUrdu = "ثابت گانٹھوں سے پسوائی گئی گہرے قدرتی رنگ اور خوشبو والی خالص ہلدی۔ کسی بھی مصنوعی رنگ سے پاک۔",
            basePrice = 150.0,
            defaultUnit = "250 گرام",
            unitOptions = listOf(
                ProductUnitOption("250 گرام", 1.0, "250 گرام پاؤچ"),
                ProductUnitOption("500 گرام", 1.9, "آدھا کلو جار")
            ),
            originOrGuarantee = "بغیر ملاوٹ و رنگ"
        ),
        Product(
            id = "prod_lal_mirch",
            nameUrdu = "لال مرچ پاؤڈر (تیز و خوش رنگ)",
            nameEnglish = "Red Chili Powder",
            categoryId = "cat_spices",
            descriptionUrdu = "کنری سندھ کی منتخب شدہ لال مرچوں کا باریک پاؤڈر۔ سالن میں قدرتی رنگت اور متوازن تیزی لائے۔",
            basePrice = 180.0,
            defaultUnit = "250 گرام",
            unitOptions = listOf(
                ProductUnitOption("250 گرام", 1.0, "250 گرام پاؤچ"),
                ProductUnitOption("500 گرام", 1.95, "آدھا کلو جار")
            ),
            originOrGuarantee = "خالص ثابت مرچ پسوائی"
        ),
        Product(
            id = "prod_garam_masala",
            nameUrdu = "شاہی ثابت و پسا گرم مصالحہ",
            nameEnglish = "Shahi Garam Masala Blend",
            categoryId = "cat_spices",
            descriptionUrdu = "زیرہ، کالی مرچ، دار چینی، بڑی الائچی اور لونگ کا متوازن روایتی پنجابی مرکب۔",
            basePrice = 240.0,
            defaultUnit = "150 گرام",
            unitOptions = listOf(
                ProductUnitOption("150 گرام", 1.0, "150 گرام ائیر ٹائٹ جار"),
                ProductUnitOption("300 گرام", 1.9, "300 گرام جار")
            ),
            originOrGuarantee = "اصلی جڑی بوٹیوں سے تیار"
        ),

        // ماہانہ راشن پیکجز (Monthly Ration Bundles)
        Product(
            id = "prod_ration_family",
            nameUrdu = "ماہانہ مکمل فیملی راشن پیکج (5 تا 7 افراد)",
            nameEnglish = "Monthly Family Ration Bundle",
            categoryId = "cat_ration",
            descriptionUrdu = "پورے مہینے کا کفایتی راشن پیکج:\n• چکی کا آٹا 20 کلو\n• ڈالڈا ویجیٹیبل آئل 5 لیٹر\n• سپر باسمتی چاول 5 کلو\n• سفید چینی 5 کلو\n• دال چنا 1 کلو + دال مسور 1 کلو\n• کڑک کینیا چائے 450 گرام\n• لاہوری نمک 1 پیکٹ\nلودھراں شہر میں مفت سپیشل کارگو ڈیلیوری!",
            basePrice = 8850.0,
            defaultUnit = "مکمل ماہانہ فیملی پیکج",
            unitOptions = listOf(
                ProductUnitOption("مکمل ماہانہ فیملی پیکج", 1.0, "مکمل پیکج (بوری و باکس)")
            ),
            originOrGuarantee = "پیور فوڈ لودھراں کا خصوصی ڈسکاؤنٹ پیکج",
            isFeatured = true,
            isRationPackage = true
        ),
        Product(
            id = "prod_ration_small",
            nameUrdu = "بنیادی چھوٹا راشن پیکج (2 تا 3 افراد)",
            nameEnglish = "Essential Starter Ration Bundle",
            categoryId = "cat_ration",
            descriptionUrdu = "چھوٹے گھرانوں کیلئے ضروری ماہانہ راشن:\n• چکی کا آٹا 10 کلو\n• کوکنگ آئل 2.5 لیٹر\n• باسمتی چاول 2.5 کلو\n• سفید چینی 2 کلو\n• دال چنا آدھا کلو + دال مونگ آدھا کلو\n• چائے 250 گرام + نمک 1 پیکٹ",
            basePrice = 4750.0,
            defaultUnit = "چھوٹا بنیادی راشن پیکج",
            unitOptions = listOf(
                ProductUnitOption("چھوٹا بنیادی راشن پیکج", 1.0, "ایک باکس میں پیک")
            ),
            originOrGuarantee = "بچت اور سہولت کی گارنٹی",
            isFeatured = true,
            isRationPackage = true
        ),

        // گھریلو ضروریات (Household Essentials)
        Product(
            id = "prod_washing_powder",
            nameUrdu = "ایکسپریس واشنگ پاؤڈر (سرف)",
            nameEnglish = "Express Laundry Washing Powder",
            categoryId = "cat_household",
            descriptionUrdu = "کپڑوں سے ضدی داغ مٹانے اور خوشبو کیلئے طاقتور لیمن واشنگ پاؤڈر۔",
            basePrice = 380.0,
            defaultUnit = "1 کلوگرام",
            unitOptions = listOf(
                ProductUnitOption("1 کلوگرام", 1.0, "1 کلو پاؤچ"),
                ProductUnitOption("3 کلوگرام", 2.85, "3 کلو فیملی بوری")
            ),
            originOrGuarantee = "باصلاحیت اینزائم فارمولا"
        ),
        Product(
            id = "prod_dishwash_bar",
            nameUrdu = "لیمن ڈش واش بار صابن (بنڈل)",
            nameEnglish = "Lemon Dishwash Bar (Pack of 3)",
            categoryId = "cat_household",
            descriptionUrdu = "چکنائی فوری دور کرنے والا برتن دھونے کا لیمن بار صابن۔ 3 ٹکیاں بنڈل۔",
            basePrice = 140.0,
            defaultUnit = "3 ٹکیاں بنڈل",
            unitOptions = listOf(
                ProductUnitOption("3 ٹکیاں بنڈل", 1.0, "3 ٹکیاں پیک")
            ),
            originOrGuarantee = "تیز جھاگ اور کفایت"
        )
    )
}
