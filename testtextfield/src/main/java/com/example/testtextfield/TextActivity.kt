package com.example.testtextfield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testtextfield.ui.theme.SimpleColorSelectorTheme

class TextActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleColorSelectorTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MyInput()
                }
            }
        }
    }
}
/*
MyInput
-   >textState
-   MyTextField  
-   MyAddButton
-   MyText


 */

@Preview
@Composable
fun MyInput(){
    var text by remember { mutableStateOf("")}
    Column() {
        MyTextField(text = text,onTextChange = {text = it})
        MyAddButton(
            onClick = {
                text = ""
            },
            text = "ClearButton",
            modifier = Modifier.padding(all = 2.dp)
        )
    }
}

@Composable
fun MyTextField(text:String, onTextChange:(String) -> Unit){
    TextField(
        value = text,
        onValueChange = onTextChange,
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent
        )
    )
}

@Composable
fun MyAddButton(
    onClick:() -> Unit,
    text:String,
    modifier: Modifier = Modifier,
    enabled:Boolean = true
){
    TextButton(
        onClick = onClick,
        shape = CircleShape,
        enabled = enabled,
        modifier = modifier
    ){
        Text(text)
    }
}

@Composable
fun MyText(){
    
}


