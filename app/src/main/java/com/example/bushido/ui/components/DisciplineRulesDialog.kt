package com.example.bushido.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.bushido.ui.theme.*

@Composable
fun DisciplineRulesDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f),
            shape = RoundedCornerShape(20.dp),
            color = CardFloating,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderStandard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "بستن",
                            tint = TextSecondary
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "مرام‌نامه و ۴ اصل بنیادین بوشیدو",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "اصول حاکم بر میدان نبرد ۹۰ روزه",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(CardInner, RoundedCornerShape(10.dp))
                                .border(1.dp, BorderStandard, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Gavel,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                HorizontalDivider(color = BorderStandard, modifier = Modifier.padding(vertical = 8.dp))

                // Rules Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RuleCard(
                        number = "۱",
                        title = "اصل صداقت بی‌رحمانه در ثبت داده‌ها",
                        description = "یک روز سوخته با ثبت واقعی، شرف دارد به روزی که با تیک‌های دروغین و توهمی پر شده باشد. بوشیدو بر مبنای حقیقت بنا شده است."
                    )
                    RuleCard(
                        number = "۲",
                        title = "اصل ۵ از ۵: روز استاندارد یا سوخته",
                        description = "فونداسیون یک کل تفکیک‌ناپذیر است. ثبت ۴ پایه از ۵ پایه همچنان روز سوخته است تا ذهن به کم‌کاری جزئی خو نگیرد و برای کمال بجنگد."
                    )
                    RuleCard(
                        number = "۳",
                        title = "اصل قفل سیستم در برابر بدهی‌های گذشته",
                        description = "سیستم اجازه ثبت روز جاری را بدون کالبدشکافی و بستن پرونده افت‌های گذشته نمی‌دهد. فرار از بازبینی گذشته در مرام سامورایی جایی ندارد."
                    )
                    RuleCard(
                        number = "۴",
                        title = "اصل فریز شخصی در شرایط اضطرار واقعی",
                        description = "اگر شرایط غیرمترقبه و اضطراری رخ داد، فریز شخصی زنجیره شما را محافظت می‌کند تا بدون اضطراب بی‌مورد دوباره به خط بازگردید."
                    )
                }

                HorizontalDivider(color = BorderStandard, modifier = Modifier.padding(vertical = 8.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DisciplineCrimson,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "متوجه شدم و متعهدم",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun RuleCard(
    number: String,
    title: String,
    description: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = CardInner,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderStandard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Right
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(DisciplineCrimson.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                        .border(1.dp, DisciplineCrimson.copy(alpha = 0.4f), RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = number,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = DisciplineCrimson
                    )
                }
            }
            Text(
                text = description,
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 18.sp,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
