// file: HomeScreen.kt
package com.example.vidyaksha.presentation.home

import com.example.vidyaksha.data.local.AttachmentEntity
import androidx.compose.animation.core.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.*
import com.example.vidyaksha.R
import com.example.vidyaksha.data.local.NoteEntity
import com.example.vidyaksha.presentation.components.BrainfireCard
import com.example.vidyaksha.presentation.notes.AddEditNoteScreen
import com.example.vidyaksha.presentation.notes.AnimatedVerticalNoteList
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import java.time.LocalDate

@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val notes by viewModel.notes.collectAsState(initial = emptyList())

    val dailyFacts = remember { getDailyFacts() }
    val todayIndex = LocalDate.now().dayOfYear % dailyFacts.size
    val currentFact = dailyFacts[todayIndex]

    var showEditor by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<NoteEntity?>(null) }

    val archivoBlack = FontFamily(Font(R.font.archivo_black))
    val textColor = if (isSystemInDarkTheme()) Color.White else Color(0xFF444444)

    Scaffold(
        floatingActionButton = {
            if (!showEditor) { // 👈 Hide FAB when editor is open
                FloatingActionButton(
                    onClick = {
                        editingNote = null
                        showEditor = true
                    },
                    containerColor = Color(0xFF7C4DFF),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Note")
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFFF9E3), Color(0xFFF8F8FF))
                )
            )
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            // Header
            BannerHeaderSmall(archivoBlack)
            Spacer(modifier = Modifier.height(5.dp))
            // Continue learning progress bar
            ContinueLearningBar(modifier = Modifier.padding(vertical = 5.dp))
            Spacer(modifier = Modifier.height(5.dp))
            // Daily fact card
            BrainfireCard(fact = currentFact)
            Spacer(modifier = Modifier.height(5.dp))


            Text(
                text = "Your Notes",
                fontSize = 22.sp,
                color = textColor,
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            if (notes.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "✨ No notes yet — tap + to add your first thought!",
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                AnimatedVerticalNoteList(
                    notes = notes,
                    onOpen = { note ->
                        editingNote = note
                        showEditor = true
                    },
                    onDelete = { note ->
                        viewModel.deleteNote(note)
                    }
                )
            }

            Spacer(modifier = Modifier.height(100.dp)) // bottom space for FAB
        }

        if (showEditor) {
            AddEditNoteOverlay(
                existingNote = editingNote,
                onSaveWithAttachments = { note, toInsert, toDelete ->
                    if (editingNote == null) {
                        // Save new note
                        viewModel.addNoteWithAttachments(note, toInsert)
                    } else {
                        // Update existing note
                        viewModel.updateNoteWithAttachments(note, toInsert, toDelete)
                    }
                    showEditor = false
                },
                onCancel = { showEditor = false }
            )
        }
    }
}

@Composable
private fun BannerHeaderSmall(font: FontFamily) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            text = "Soul breathing",
            fontFamily = font,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFBFC5CF),
            letterSpacing = 0.5.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Then keep learning",
            fontFamily = font,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFBFC5CF),
            letterSpacing = 0.3.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ContinueLearningBar(modifier: Modifier = Modifier) {
    val isDark = isSystemInDarkTheme()

    val infiniteTransition = rememberInfiniteTransition(label = "progress_loop")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress_value"
    )

    val barHeight = 12.dp
    val boySize = 36.dp

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Continue Learning",
            fontSize = 18.sp,
            color = if (isDark) Color.White else Color(0xFF444444),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(boySize),
            contentAlignment = Alignment.CenterStart
        ) {
            val barWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
            val animatedOffset = with(LocalDensity.current) { (progress * (barWidthPx - 40)).toDp() }

            // Progress bar background
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(barHeight)
                    .align(Alignment.CenterStart)
                    .clip(RoundedCornerShape(50)),
                color = Color(0xFF7C4DFF),
                trackColor = Color(0xFFE0E0E0)
            )

            // Lottie animation perfectly centered on bar
            val composition by rememberLottieComposition(LottieCompositionSpec.Asset("running_boy.json"))
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .size(boySize)
                    .offset(x = animatedOffset, y = (-barHeight / 2))
                    .align(Alignment.CenterStart)
            )
        }
    }
}

@Composable
private fun AddEditNoteOverlay(
    existingNote: NoteEntity?,
    onSaveWithAttachments: (NoteEntity, List<AttachmentEntity>, List<AttachmentEntity>) -> Unit,
    onCancel: () -> Unit
) {
    // Example: you can load existing attachments here from ViewModel or Repo later
    val attachmentsFromDb = remember { mutableStateListOf<AttachmentEntity>() }

    Surface(
        color = Color.White.copy(alpha = 0.98f),
        modifier = Modifier.fillMaxSize()
    ) {
        AddEditNoteScreen(
            existingNote = existingNote,
            existingAttachments = attachmentsFromDb,
            onSaveWithAttachments = onSaveWithAttachments,
            onBack = onCancel
        )
    }
}


fun getDailyFacts(): List<String> {
    return listOf(
        "Learning every day compounds into mastery.",
        "Curiosity keeps your brain young.",
        "Consistency is a form of intelligence.",
        "Knowledge compounds faster than money.",
        "The mind grows by feeding it ideas.",
        "Money is a tool, not the goal.",
        "Invest early, compound endlessly.",
        "Wealth rewards patience, not impulse.",
        "Markets move money from impatient to patient.",
        "Every rupee saved is a soldier for freedom.",
        "Your habits build your fortune.",
        "Compounding is the eighth wonder of the world.",
        "Investing is simple, not easy.",
        "Inflation quietly steals your wealth.",
        "Buy assets, not liabilities.",
        "Wealth whispers, it never shouts.",
        "Be fearful when others are greedy.",
        "Be greedy when others are fearful.",
        "Time in the market beats timing the market.",
        "Budgeting is your first investment plan.",
        "Financial freedom starts with discipline.",
        "Wealth flows to those who respect time.",
        "Spend less than you earn, invest the rest.",
        "Don’t chase money, chase value.",
        "Assets feed you, liabilities bleed you.",
        "Cash flow is the heartbeat of freedom.",
        "Your mindset is your best investment.",
        "Riches fade, wealth endures.",
        "Your money should make money while you sleep.",
        "Risk comes from not knowing what you’re doing.",
        "The first rule is to never lose money.",
        "The second rule is to remember the first rule.",
        "Don’t save what is left after spending.",
        "Spend what is left after saving.",
        "Price is what you pay, value is what you get.",
        "Diversify, but don’t overcomplicate.",
        "Consistency beats intensity in investing.",
        "A budget tells your money where to go.",
        "Passive income buys time.",
        "Freedom is the ultimate dividend.",
        "Knowledge is the best compound interest.",
        "Your salary can make you comfortable.",
        "Your investments make you free.",
        "Avoid lifestyle inflation.",
        "Your network can multiply your net worth.",
        "Fear and greed drive markets — master both.",
        "Wealth grows in silence.",
        "Traders manage risk, gamblers chase luck.",
        "Wealth is not income, it’s freedom.",
        "Make money work harder than you do.",
        "Luck is preparation meeting opportunity.",
        "You are one decision away from change.",
        "Compounding is slow, then suddenly exponential.",
        "Start small, but start now.",
        "Your future self will thank your present discipline.",
        "Rich people acquire assets.",
        "Poor people acquire liabilities.",
        "The goal isn’t more money, it’s more options.",
        "Learn to live below your means.",
        "Invest in what you understand.",
        "Debt can be a tool or a trap.",
        "Inflation punishes the hoarder.",
        "Freedom beats luxury every time.",
        "Money amplifies your character.",
        "Focus on cash flow before luxury.",
        "Spend intentionally, not emotionally.",
        "Learn, earn, return.",
        "Your wealth mirrors your habits.",
        "Money flows where attention goes.",
        "Patience turns volatility into profit.",
        "Markets reward the informed, not the emotional.",
        "You can’t beat the market by watching it.",
        "Discipline beats prediction.",
        "The best investors are learners, not prophets.",
        "Diversification is your parachute.",
        "No plan survives emotion — automate it.",
        "Invest with reason, not reaction.",
        "The earlier you start, the luckier you get.",
        "Protect the downside; profits will follow.",
        "Your savings rate matters more than your income.",
        "Don’t rent your lifestyle on borrowed time.",
        "Retire early by living wisely.",
        "Opportunities come to the prepared.",
        "A bear market creates millionaires quietly.",
        "Winners plan; losers hope.",
        "FOMO is the fastest path to loss.",
        "You don’t need to predict; you need to prepare.",
        "Make your money a silent employee.",
        "Wealth grows from patience, not panic.",
        "Avoid get-rich-quick; choose get-rich-sure.",
        "You can’t manage what you don’t measure.",
        "Every expense has an opportunity cost.",
        "Investing is personal, not universal.",
        "The goal is to own income-producing assets.",
        "Build skills, not just savings.",
        "Risk what you can afford to lose.",
        "Plan for the worst, invest for the best.",
        "Your portfolio reflects your mindset.",
        "An investor reads more than he trades.",
        "Warren Buffett reads 500 pages a day.",
        "Never stop learning money psychology.",
        "Wealth is built slowly, lost quickly.",
        "Don’t just earn money, multiply it.",
        "Hustle builds capital, patience builds wealth.",
        "Your 20s are for learning, 30s for earning, 40s for compounding.",
        "Market corrections are discounts for the disciplined.",
        "Time turns volatility into opportunity.",
        "Financial education is lifelong armor.",
        "Create more than you consume.",
        "Financial peace beats financial show-off.",
        "Broke is temporary, poor is mental.",
        "Your thoughts attract your wealth.",
        "Money follows clarity, not confusion.",
        "Every market cycle teaches humility.",
        "Save aggressively, invest intelligently.",
        "Cash is not king — cash flow is.",
        "The first wealth is health.",
        "Freedom costs less than regret.",
        "Invest like a farmer, not a gambler.",
        "Seeds take time to grow — so does wealth.",
        "You can’t pour from an empty wallet.",
        "Avoid comparison — run your own race.",
        "A side hustle funds your dreams.",
        "Compounding rewards consistency, not perfection.",
        "Track your money or it will track you.",
        "Real wealth is having time to think.",
        "Don’t buy it twice — once with money, again with regret.",
        "Wealthy people think in decades.",
        "Small leaks sink big ships.",
        "Financial goals give purpose to discipline.",
        "If you can’t control emotion, you can’t control money.",
        "A budget is your financial GPS.",
        "Inflation never sleeps — neither should your portfolio.",
        "The rich invest in education, the poor in entertainment.",
        "Liquidity is safety in disguise.",
        "If you don’t plan your money, someone else will.",
        "Compound interest is time’s love language.",
        "Investing is emotional control disguised as math.",
        "The more you learn, the more you earn.",
        "Your mindset compounds faster than your money.",
        "Saving is defense; investing is offense.",
        "Always pay yourself first.",
        "A bull market makes everyone look smart.",
        "Only bear markets reveal skill.",
        "Risk tolerance grows with knowledge.",
        "If it sounds too good, it is.",
        "The hardest part is holding when others panic.",
        "Long-term wealth requires short-term patience.",
        "Focus on process, not prediction.",
        "No investor ever went broke taking profit.",
        "Liquidity buys options, not happiness.",
        "Hype fades, fundamentals stay.",
        "The best investment is in yourself.",
        "Don’t outsource thinking to influencers.",
        "Track performance, not opinions.",
        "Don’t let taxes dictate your decisions.",
        "Greed kills portfolios faster than crashes.",
        "Compounding works in life too — skills, trust, knowledge.",
        "The rich own time, not things.",
        "Freedom is built, not bought.",
        "Your returns mirror your patience.",
        "Buy fear, sell excitement.",
        "Long-term investing is emotional mastery.",
        "Every crash is a sale for the brave.",
        "Inflation is silent taxation.",
        "The market rewards courage and punishes arrogance.",
        "Risk is the price of opportunity.",
        "You can’t time luck, but you can prepare for it.",
        "Avoid lifestyle creep — it’s the thief of freedom.",
        "Build systems, not hope.",
        "Think value, not price.",
        "Don’t invest in trends, invest in truth.",
        "Simplify — complexity hides risk.",
        "The calm investor wins the war.",
        "Use credit smartly — as leverage, not comfort.",
        "The best traders master boredom.",
        "Money saved is power stored.",
        "Financial wisdom is a daily habit.",
        "You don’t need more money, you need more intention.",
        "Discipline creates freedom, emotion destroys it.",
        "The rich ask how to grow, not how to spend.",
        "Learn to say no to instant pleasure.",
        "Your money story shapes your destiny.",
        "Work for knowledge, not just paychecks.",
        "Retirement is not age — it’s autonomy.",
        "Abundance starts in the mind.",
        "Think long term, act consistently.",
        "In trading, survival is victory.",
        "Don’t confuse activity with achievement.",
        "Fortunes favor the financially literate.",
        "Your money habits predict your future.",
        "Inflation erases the lazy investor.",
        "The safest investment is self-awareness.",
        "Money freedom is mindset freedom.",
        "You can’t diversify ignorance.",
        "Financial independence is the new status symbol.",
        "Earn in silence, spend with purpose.",
        "Every market teaches the humble investor.",
        "Keep cash for courage.",
        "The wealthy plan decades ahead.",
        "Being broke once is experience, twice is a lesson ignored.",
        "The best return is peace of mind.",
        "Markets test patience more than intelligence.",
        "Fact: Compounding is the eighth wonder of the world — time multiplies money quietly.",
        "Fact: Starting early beats starting big in investing.",
        "Fact: The stock market rewards patience, not prediction.",
        "Myth: You need a lot of money to start investing.",
        "Fact: Even small consistent investments can grow into wealth over decades.",
        "Myth: Day trading is the fastest path to riches.",
        "Fact: Wealth creation comes from consistency, not timing.",
        "Fact: Diversification protects your capital during uncertain times.",
        "Myth: The stock market is just gambling.",
        "Fact: Investing is ownership; gambling is speculation.",
        "Fact: Inflation silently reduces your purchasing power every year.",
        "Myth: Keeping money in savings protects it from loss.",
        "Fact: Compound interest works best with time, not luck.",
        "Myth: Only experts can beat the market.",
        "Fact: Most investors fail when they chase trends.",
        "Fact: The best investors are lifelong learners.",
        "Myth: You can get rich quickly from stocks.",
        "Fact: The market transfers wealth from the impatient to the patient.",
        "Fact: A budget is the foundation of wealth creation.",
        "Myth: High risk always means high return.",
        "Fact: Smart risk is measured; blind risk is gambling.",
        "Fact: Financial freedom means control, not luxury.",
        "Myth: You need a finance degree to invest well.",
        "Fact: Simplicity often beats complexity in investing.",
        "Fact: Reinvesting dividends accelerates long-term growth.",
        "Myth: Gold is the safest investment forever.",
        "Fact: True diversification includes stocks, bonds, and real assets.",
        "Myth: You can predict short-term market moves accurately.",
        "Fact: Even professionals can’t consistently time the market.",
        "Fact: Index funds outperform most active investors over time.",
        "Myth: Only the rich can afford to invest.",
        "Fact: The best time to start was yesterday. The next best is today.",
        "Fact: The more you learn, the less you fear market swings.",
        "Myth: Debt is always bad.",
        "Fact: Smart leverage can build wealth; reckless debt destroys it.",
        "Fact: Financial goals create purpose for your money.",
        "Myth: Real estate always goes up.",
        "Fact: Every asset class goes through cycles.",
        "Fact: Emotional investing leads to poor decisions.",
        "Myth: You must watch your stocks daily.",
        "Fact: Long-term investors rarely panic in short-term noise.",
        "Fact: The market rewards discipline, not excitement.",
        "Myth: You can’t lose money in mutual funds.",
        "Fact: All investments carry some level of risk.",
        "Fact: Cash flow management matters more than flashy returns.",
        "Myth: More income automatically means more wealth.",
        "Fact: Saving habits matter more than salary level.",
        "Fact: Compounding interest rewards the patient saver.",
        "Myth: The stock market is only for the young.",
        "Fact: Anyone can invest at any age with proper strategy.",
        "Fact: Knowledge is your best asset in finance.",
        "Myth: You can’t invest during a recession.",
        "Fact: Down markets create opportunities for patient investors.",
        "Fact: Wealth creation is a long game, not a sprint.",
        "Myth: You must have insider info to profit.",
        "Fact: Public information and discipline are enough to grow wealth.",
        "Fact: The market is volatile, but trends upward over decades.",
        "Myth: You need to pick winning stocks to grow rich.",
        "Fact: Consistent investing in index funds beats most traders.",
        "Fact: Emergency funds are your first investment in security.",
        "Myth: Cutting expenses alone will make you rich.",
        "Fact: Earning, saving, and investing together create wealth.",
        "Fact: Financial education pays the highest lifelong return.",
        "Myth: You can copy others’ portfolios and succeed.",
        "Fact: Every investor’s journey depends on unique goals and risk.",
        "Fact: Fear and greed drive market cycles.",
        "Myth: Market crashes destroy wealth permanently.",
        "Fact: Recovery always follows panic in resilient economies.",
        "Fact: Investing early lets compounding work its magic longer.",
        "Myth: You must time the market perfectly to profit.",
        "Fact: Time in the market beats timing the market.",
        "Fact: Inflation erodes idle savings quietly but steadily.",
        "Myth: The government will take care of your retirement.",
        "Fact: Retirement security is built by consistent planning.",
        "Fact: Investing teaches patience, perspective, and discipline.",
        "Myth: Mutual funds are risk-free investments.",
        "Fact: All investments require awareness and monitoring.",
        "Fact: Passive income gives financial independence, not overnight success.",
        "Myth: You can’t invest if you have loans.",
        "Fact: Balanced budgeting allows investing while managing debt.",
        "Fact: Compounding multiplies consistency more than capital.",
        "Myth: Diversification limits profit.",
        "Fact: Diversification limits loss and protects capital.",
        "Fact: Wealth grows faster when you automate savings.",
        "Myth: The market is rigged against small investors.",
        "Fact: Access to information and index investing levels the field.",
        "Fact: Rich people think long-term; poor people think next payday.",
        "Myth: Crypto will replace all traditional investing.",
        "Fact: Blockchain is a tool, not a guaranteed fortune.",
        "Fact: Your financial mindset decides your wealth trajectory.",
        "Myth: Credit cards are evil.",
        "Fact: Responsible credit use builds financial trust.",
        "Fact: Compounding doesn’t work without consistency.",
        "Myth: If a stock fell, it must rebound.",
        "Fact: Some companies never recover from bad fundamentals.",
        "Fact: Investing without goals is like sailing without a map.",
        "Myth: More trades mean more profit.",
        "Fact: Fewer, smarter moves compound wealth faster.",
        "Fact: Patience in investing is the hardest but most rewarding skill.",
        "Myth: Stocks are too risky compared to cash.",
        "Fact: Over long periods, equities outperform inflation and cash.",
        "Fact: The first step to wealth is awareness of expenses.",
        "Myth: Budgeting limits freedom.",
        "Fact: Budgeting creates financial freedom through control.",
        "Fact: Asset allocation is more important than stock picking.",
        "Myth: Investing is only about returns.",
        "Fact: Risk management is equally important as returns.",
        "Fact: You don’t need to beat the market; just join it.",
        "Myth: Saving small amounts doesn’t matter.",
        "Fact: Small, consistent savings create big outcomes over decades.",
        "Fact: Learning from losses makes you a better investor.",
        "Myth: Professional advisors always outperform DIY investors.",
        "Fact: Low-cost index investing beats most paid advice.",
        "Fact: Investing is about probabilities, not certainties.",
        "Myth: Markets move randomly with no logic.",
        "Fact: Fundamentals drive long-term trends despite short-term noise.",
        "Fact: Avoid debt that doesn’t generate income.",
        "Myth: You can buy happiness with wealth.",
        "Fact: Wealth amplifies who you already are.",
        "Fact: Rich habits create rich outcomes.",
        "Myth: Luck determines financial success.",
        "Fact: Discipline, patience, and education create lasting wealth.",
        "Fact: A financial plan is a lifelong roadmap.",
        "Myth: Insurance is unnecessary for the young.",
        "Fact: Insurance protects your future earning power.",
        "Fact: Markets reward consistent learners, not emotional traders.",
        "Myth: You can’t lose in real estate.",
        "Fact: Location, timing, and management determine real estate success.",
        "Fact: Financial literacy should start in school.",
        "Myth: Investing is too complicated for beginners.",
        "Fact: Simplicity often leads to the best results.",
        "Fact: Wealthy people prioritize assets, not expenses.",
        "Myth: A higher income guarantees wealth.",
        "Fact: Lifestyle inflation destroys wealth silently.",
        "Fact: Saving is earning; investing is growing.",
        "Myth: Markets crash permanently after recessions.",
        "Fact: Every downturn has been followed by recovery historically.",
        "Fact: A growth mindset compounds wealth mentally and financially.",
        "Myth: Credit scores don’t matter unless you borrow.",
        "Fact: Good credit opens opportunities even without debt.",
        "Fact: Time multiplies both wealth and mistakes.",
        "Myth: You can retire on one big win.",
        "Fact: Sustainable wealth comes from multiple consistent wins.",
        "Fact: Money works best when given direction.",
        "Myth: Budgeting is for the poor.",
        "Fact: Every millionaire tracks their spending intentionally.",
        "Fact: Knowledge is the best hedge against market risk.",
        "Myth: Bonds are always safer than stocks.",
        "Fact: Bonds carry inflation and interest rate risk too.",
        "Fact: Automating investments removes emotional errors.",
        "Myth: Financial freedom means not working at all.",
        "Fact: Financial freedom means choosing what work you do.",
        "Fact: Every dollar saved is a soldier for your goals.",
        "Myth: Rich people don’t worry about money.",
        "Fact: Wealthy people manage risk, not ignore it.",
        "Fact: Investing in yourself yields the best returns.",
        "Myth: Markets always move on logic.",
        "Fact: Emotions drive short-term market movements more than logic.",
        "Fact: The habit of saving is more important than the amount saved.",
        "Myth: Compounding works only for the rich.",
        "Fact: Compounding works for anyone who starts early and stays consistent.",
        "Fact: The best investors are calm during chaos.",
        "Myth: Investing stops after retirement.",
        "Fact: Managing your portfolio continues for life.",
        "Fact: True wealth includes time, freedom, and peace.",
        "Myth: You must sacrifice happiness to build wealth.",
        "Fact: Balance creates sustainable success.",
        "Fact: Don’t count returns; count years of discipline.",
        "Myth: Only new tech stocks make millionaires.",
        "Fact: Old, boring businesses often generate stable compounding.",
        "Fact: Greed destroys faster than loss does.",
        "Myth: The economy and stock market are the same.",
        "Fact: Markets reflect future expectations, not current events."
    )
}
