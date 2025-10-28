package com.example.vidyaksha.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vidyaksha.data.local.NoteEntity
import com.example.vidyaksha.presentation.components.AddOrEditNoteDialog
import com.example.vidyaksha.presentation.components.BrainfireCard
import com.example.vidyaksha.presentation.components.NoteCard
import com.ramcosta.composedestinations.annotation.Destination
import java.time.LocalDate

@Destination
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val notes by viewModel.notes.collectAsState(initial = emptyList())

    val dailyFacts = listOf(
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

    val todayIndex = (LocalDate.now().dayOfYear % dailyFacts.size)
    val currentFact = dailyFacts[todayIndex]

    var showDialog by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<NoteEntity?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { editingNote = null; showDialog = true },
                containerColor = Color(0xFF7C4DFF),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFFFFF9E3), Color(0xFFF8F8FF)))
            )
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                // ✅ Styled Quote Section — like Blinkit banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, fontSize = 50.sp, color = Color(0xFF222222))) {
                                    append("Soul breathing?")
                                }
                            },
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, fontSize = 34.sp, color = Color(0xFF222222))) {
                                    append("Then keep learning! ")
                                }
                                withStyle(style = SpanStyle(fontSize = 34.sp,color = Color(0xFFFF5252))) {
                                    append("☠\uFE0F")
                                }
                            },
                            textAlign = TextAlign.Center
                        )
                    }
                }

                BrainfireCard(fact = currentFact)
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Notes",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color(0xFF222222),
                )
            }

            if (notes.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✨ No notes yet — tap + to add your first thought!",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }
            } else {
                items(notes.size) { index ->
                    val note = notes[index]
                    val color = remember {
                        listOf(
                            Color(0xFFB3E5FC),
                            Color(0xFFFFCDD2),
                            Color(0xFFC8E6C9),
                            Color(0xFFFFF9C4),
                            Color(0xFFD1C4E9),
                            Color(0xFFB3E5FC), // Original Sky Blue
                            Color(0xFFFFCDD2), // Original Soft Pink
                            Color(0xFFC8E6C9), // Original Green
                            Color(0xFFFFF9C4), // Original Yellow
                            Color(0xFFD1C4E9), // Original Purple

                            Color(0xFF81D4FA), // Light Blue
                            Color(0xFFF8BBD0), // Light Pink
                            Color(0xFFA5D6A7), // Mint Green
                            Color(0xFFFFF59D), // Soft Yellow
                            Color(0xFFCE93D8), // Lavender
                            Color(0xFFFFCC80), // Peach
                            Color(0xFF9FA8DA), // Indigo
                            Color(0xFFC5E1A5), // Sage
                            Color(0xFFE0E0E0), // Light Gray
                            Color(0xFFD7CCC8)  // Dusty Lavender
                            // Add more colors as needed
                        ).random()
                    }
                    NoteCard(
                        title = note.title.ifBlank { "Untitled" },
                        description = note.content,
                        backgroundColor = color,
                        onEdit = { editingNote = note; showDialog = true },
                        onDelete = { viewModel.deleteNote(note) }
                    )
                }
            }
        }

        if (showDialog) {
            AddOrEditNoteDialog(
                existingNote = editingNote,
                onDismiss = { showDialog = false },
                onSave = { title, desc ->
                    if (editingNote == null)
                        viewModel.addNote(title, desc)
                    else
                        viewModel.updateNote(editingNote!!.copy(title = title, content = desc))
                    showDialog = false
                }
            )
        }
    }
}
