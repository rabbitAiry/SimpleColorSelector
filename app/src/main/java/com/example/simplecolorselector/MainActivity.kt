package com.example.simplecolorselector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simplecolorselector.ui.theme.SimpleColorSelectorTheme
import java.lang.NumberFormatException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleColorSelectorTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Main()
                }
            }
        }
    }
}

@Composable
fun Main(){
    val baseColor = Color.White
    var selectedColor by remember{ mutableStateOf("ffffff") }
    var resultColor by remember{ mutableStateOf(baseColor) }
    val oppositeColor = if(resultColor.luminance()>0.5) Color.Black else Color.White
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ColorSelector",
                        modifier = Modifier.padding(start = 16.dp),
                        color = oppositeColor
                    )
                },
                backgroundColor = resultColor
            )
        }
    ) {
        Content(
            selectedColor = selectedColor,
            resultColor = resultColor,
            oppositeColor = oppositeColor,
            onSelectedColorChange = {selectedColor = it},
            onRefreshButtonClick = {
                selectedColor = ""
                resultColor = baseColor
            },
            onSetColorButtonClick = {
                try {
                    if (selectedColor.length == 6){
                        val data = Integer.parseInt(selectedColor,16)
                        resultColor = Color(0xFF000000+data)
                    }else{
                        selectedColor = ""
                    }
                }catch (e:NumberFormatException){
                    selectedColor = ""
                }
            },
            onGetSuggestColor = {
                selectedColor = it
                val data = Integer.parseInt(selectedColor,16)
                resultColor = Color(0xFF000000+data)
            }
        )
    }
}


@Composable
fun Content(
    selectedColor: String,
    resultColor: Color,
    oppositeColor: Color,
    onSelectedColorChange: (String) -> Unit,
    onRefreshButtonClick: () -> Unit,
    onSetColorButtonClick: () -> Unit,
    onGetSuggestColor: (String) -> Unit
){
    val listColor:List<String> = listOf("ffea00","ffc400","00e676","2979ff","f50057","d500f9","651fff","c6ff00")
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.weight(3f)) {
            Column(
                modifier = Modifier
                    .weight(4f)
                    .fillMaxWidth()
            ) {
                Text(text = "输入十六进制颜色",
                    modifier = Modifier.padding(start = 24.dp,top = 36.dp,end = 12.dp,bottom = 2.dp),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h6)
                Text(
                    text = "e.g: #accccc",
                    modifier = Modifier.padding(start = 24.dp),
                    color = Color.Gray
                )
                AddColorInput(
                    selectedColor = selectedColor,
                    resultColor = resultColor,
                    oppositeColor = oppositeColor,
                    onSelectedColorChange = onSelectedColorChange,
                    onSetColorButtonClick = onSetColorButtonClick,
                    modifier = Modifier.padding(start = 24.dp)
                )
                Row {
                    SuggestColorHolder(
                        listColor,
                        modifier = Modifier
                            .padding(start = 24.dp, top = 16.dp, end = 24.dp)
                            .height(42.dp)
                            .fillMaxWidth(),
                        onGetSuggestColor = onGetSuggestColor
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(horizontal = 24.dp)
                    .weight(3f),
                shape = RoundedCornerShape(12.dp),
                elevation = 4.dp,
                backgroundColor = resultColor
            ){
                Box(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if(selectedColor.isNotBlank())"#$selectedColor" else "Unable to resolve",
                        style = MaterialTheme.typography.h6,
                        color = oppositeColor
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier
                .padding(all = 24.dp)
        ){
            Spacer(
                modifier = Modifier.weight(1f)
            )
            FloatingActionButton(
                backgroundColor = resultColor,
                onClick = onRefreshButtonClick) {
                Icon(
                    Icons.Sharp.Refresh,
                    contentDescription = null,
                    tint = oppositeColor
                )
            }
        }
    }
}

@Composable
fun MyTextButton(
    modifier: Modifier,
    onClickHere: () -> Unit,
    enabled :Boolean=true,
    border :BorderStroke? = null,
    text: String,
    colors: ButtonColors
    ){
    TextButton(
        modifier = modifier,
        onClick = onClickHere,
        enabled = enabled,
        shape = RoundedCornerShape(6.dp),
        border = border,
        colors = colors
    ){
        Text(text = text)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddColorInput(
    modifier: Modifier,
    selectedColor: String,
    resultColor: Color,
    oppositeColor: Color,
    onSelectedColorChange: (String) -> Unit,
    onSetColorButtonClick: () -> Unit
) {
    Row (
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
    ){
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .weight(3f)
        ){
            Text(
                modifier = Modifier.padding(bottom = 10.dp),
                text = "#",
                style = MaterialTheme.typography.h6
            )
            val keyboardController = LocalSoftwareKeyboardController.current
            TextField(
                value = selectedColor,
                onValueChange = onSelectedColorChange,
                modifier = Modifier
                    .weight(1f),
                textStyle = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    onSetColorButtonClick
                    keyboardController?.hide()
                }),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    cursorColor = resultColor,
                    focusedIndicatorColor = resultColor.copy(alpha = ContentAlpha.high)
                ),
            )
        }
        MyTextButton(
            modifier = Modifier
                .weight(2f)
                .padding(start = 24.dp),
            onClickHere = onSetColorButtonClick,
            enabled = selectedColor.isNotBlank(),
            text = "提交",
            colors = ButtonDefaults.buttonColors(
                backgroundColor = resultColor,
                contentColor = oppositeColor
            )
        )
        Spacer(modifier = Modifier.weight(2f))
    }
}

@Composable
fun SuggestColorHolder(
    colors:List<String>,
    modifier: Modifier,
    onGetSuggestColor: (String) -> Unit
){
    var isCollapsed by remember { mutableStateOf(false) }
    Row(modifier = modifier) {
        MyTextButton(
            modifier = Modifier,
            onClickHere = { isCollapsed = !isCollapsed },
            text = if(isCollapsed)"展开推荐颜色 》" else "折叠",
            border = BorderStroke(1.dp,Color.LightGray),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White
            )
        )
        if(!isCollapsed){
            LazyRow(Modifier.padding(start = 10.dp)){
                items(colors){color ->
                    MyTextButton(
                        modifier = Modifier.padding(start = 6.dp),
                        onClickHere = { onGetSuggestColor(color) },
                        text = "#$color",
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF000000+Integer.parseInt(color,16))
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview(){
    Main()
}