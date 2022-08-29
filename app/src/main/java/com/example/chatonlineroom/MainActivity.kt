package com.example.chatonlineroom

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatonlineroom.ui.theme.ChatOnlineRoomTheme
import com.example.chatonlineroom.ui.theme.CustomSecondary
import com.example.chatonlineroom.ui.theme.CustomTertiary
import com.example.chatonlineroom.ui.theme.CustomTextColor
import kotlinx.parcelize.Parcelize
import java.io.File

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatOnlineRoomTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val chatRoomViewModel by lazy { ChatRoomsRepository() }
                    val messageViewModel by lazy { MessagesRepository() }
                    val chatRoomInfo = chatRoomViewModel.getAllChatRooms()[0]
                    val id: Int = chatRoomInfo.id
                    Column {
                        Messages(messageViewModel.getAllMessages(id))
                    }
                }
            }
        }
    }
}

// Composable

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Messages(
    chatRoomMessages: List<MessageItem>
) {

    val context = LocalContext.current
    var (showMenu, setShowMenu) =  remember { mutableStateOf(false) }
    var (isMessageMenu, setIsMessageMenu) =  remember { mutableStateOf(false) }
    var (title, setTitle)  =  remember { mutableStateOf("EstEnLigne") }

    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text(
                        text = "$title",
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                Toast.makeText(context, "Not implemented.", Toast.LENGTH_SHORT).show()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Navigate Back"
                )
            }
        },
        actions = {
            IconButton(onClick = {
                Toast.makeText(context, "Not implemented.", Toast.LENGTH_SHORT).show()
            }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Icon"
                )
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = {
                    setShowMenu(false)
                }
            ) {
                    DropdownMenuItem(onClick = {
                        Toast.makeText(context, "Not yet Implemented", Toast.LENGTH_SHORT)
                            .show()
                    }) {
                        Row(
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Copy",
                                modifier = Modifier.padding(horizontal = 5.dp)
                            )
                        }

                    }
                    DropdownMenuItem(onClick = {
                        Toast.makeText(context, "Not yet Implemented!", Toast.LENGTH_SHORT)
                            .show()
                        return@DropdownMenuItem
                    }) {
                        Row(
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Copy",
                                modifier = Modifier.padding(horizontal = 5.dp)
                            )
                        }
                    }

            }
        }
    )

    Column(
        modifier = Modifier
            .padding(10.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight(.9f)
        ) {
            itemsIndexed(items = chatRoomMessages) { index, message ->
                Spacer(modifier = Modifier.width(3.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),

                    ) {
                    val isSender: Boolean = index%2==0
                    Row(modifier=Modifier

                    ){
                        NewMessage(message = message, time = "00:00", isSender = isSender,
                            modifier = Modifier.clickable {
                                Log.d("MessageClicked", "Menu State: $isMessageMenu")
                                setTitle("${message.id}")
                                setIsMessageMenu(true)
                                return@clickable
                            }
                        )
                    }

                }
            }

        }
        WriteMessage( modifier= Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max))
    }


}

@Composable
fun NewMessage(message: MessageItem?, time: String, isSender: Boolean, modifier: Modifier) {
    val BotChatBubbleShape = RoundedCornerShape(0.dp, 30.dp, 30.dp, 8.dp)
    val AuthorChatBubbleShape = RoundedCornerShape(30.dp, 0.dp, 8.dp, 30.dp)
    val hasBody: Boolean = message?.body !== null
    val context = LocalContext.current

    if (hasBody) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
                .clickable {
                    Toast
                        .makeText(context, "Not Implemented!", Toast.LENGTH_SHORT)
                        .show()
                    return@clickable
                }
            ,
            horizontalAlignment = if (isSender) Alignment.End else Alignment.Start
        ) {
            if (!isSender) {
                Text(
                    text = message?.senderName!!,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 3.dp),
                    fontSize = 15.sp
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        if (isSender) CustomSecondary else CustomTertiary,
                        shape = if (isSender) AuthorChatBubbleShape else BotChatBubbleShape
                    )
                    .padding(
                        top = 5.dp,
                        bottom = 8.dp,
                        start = 10.dp,
                        end = 10.dp
                    )
                    .widthIn(0.dp, 320.dp)

            ) {

                Column(
                    modifier = modifier
                ) {
                    message?.body?.apply {
                        Text(
                            text = this,
                            color = Color.Black
                        )
                        val topics = listOf("topic1", "topic2", "topic3")
                        LazyRow(){
                            itemsIndexed(items = topics){index, topic ->
                                Text(
                                    text = "#$topic",
                                    color = Color.Magenta,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight(500)
                                )
                                if (topics.size - 1 != index){
                                    Text(
                                        text = ", ",
                                        color = Color.Magenta,
                                        fontSize = 15.sp
                                    )
                                }

                            }
                        }

                        // This will fail as of now(No messageTags available in messages)
                        message.messageTag?.apply {
                            Text(
                                text = this.name,
                                color = Color.Magenta
                            )
                        }
                    }
                }
            }
            Text(
                text = time,
                fontSize = 13.sp,
                color = CustomTextColor,
                modifier = Modifier.padding(start = 0.dp)
            )
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WriteMessage(
    modifier:Modifier =Modifier
) {
    val messageViewModel by lazy { MessagesRepository() }
    val context = LocalContext.current
    val textMessage = remember { mutableStateOf("") }
    Card(
        modifier = modifier,
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    ) {
        OutlinedTextField(
            value = textMessage.value,
            onValueChange = {
                textMessage.value = it
            },
            shape = RoundedCornerShape(25.dp),
            placeholder = {
                Text(text = "Message", fontStyle = FontStyle(1))
            },
            leadingIcon = {
                Icon(
                    Icons.Filled.AttachFile,
                    contentDescription = "Send Icon, Click to send",
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "Unimplemented!", Toast.LENGTH_SHORT).show()
                    }
                )
            },
            trailingIcon = {
                if (textMessage.value.isNotEmpty() || textMessage.value!="") {
                    Icon(
                        Icons.Rounded.Send,
                        contentDescription = "Send Icon, Click to send",
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.clickable {
                            Toast.makeText(context, "Not yet implemented!", Toast.LENGTH_SHORT).show()
                        }

                    )
                } else {
                    Icon(
                        Icons.Rounded.Send,
                        contentDescription = "Send Icon disabled",
                        tint = Color.Gray,
                        modifier = Modifier.clickable {
                            Toast.makeText(
                                context,
                                "Message can not be empty!",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@clickable
                        }

                    )
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding((10.dp))
        )
    }
}

// Models
data class MessageItem(
    val authorId: Int?,
    val body: String?,
    val dateDeleted: String?,
    val dateRead: String?,
    val dateReceived: String?,
    val dateSent: String,
    val dateStarred: String?,
    val file: File?,
    val fileId: Int?,
    val id: Int,
    val linkedId: Int?,
    val messageTag: MessageTag?,
    val messageTagId: Int?,
    val messageType: Int?,
    val reaction: Int?,
    val receiverId: Int?,
    val senderId: Int,
    val senderName: String,
    val chatRoomId: Int
)

data class MessageItemDto(
    private val body: String,
    private val dateSent: String,
    private val senderId: Int,
    private val linkedId: Int? = null,
    private val messageTag: MessageTagDto?
)

@Parcelize
data class MessageTag(
    val chatRoomId: Int,
    val creatorId: Int? ,
    val dateCreated: String? ,
    val id: Int? ,
    val name: String ,
    val parentId: Int?
): Parcelable

@Parcelize
data class MessageTagDto(
    val chatRoomId: Int,
): Parcelable

@Parcelize
data class ChatRoom(
    val id: Int,
    val latestMessage: LatestMessage?,
    val name: String,
    val photoFileName: String?,
    val profileId: Int,
    val type: Int,
    val userBlocked: Boolean?,
    val userChatRoomId: Int,
    val userExited: Boolean?,
    val userMuted: Boolean?,
    val userPinned: Boolean?
) : Parcelable

@Parcelize
data class LatestMessage(
    val dateSent: String,
    val id: Int,
    val messageType: Int?,
    val notReadCount: Int?,
    val notReceivedCount: Int?,
    val senderId: Int,
    val shortBody: String? = ".."
): Parcelable


/// Repository
class ChatRoomsRepository {
    val rooms = listOf(
        ChatRoom(
            id = 0,
            latestMessage = null,
            name = "First",
            photoFileName = null,
            profileId = 0,
            type = 0,
            userPinned = false,
            userMuted = false,
            userExited = false,
            userBlocked = false,
            userChatRoomId = 0,
        ),
        ChatRoom(
            id = 1,
            latestMessage = null,
            name = "Second",
            photoFileName = null,
            profileId = 0,
            type = 0,
            userPinned = false,
            userMuted = false,
            userExited = false,
            userBlocked = false,
            userChatRoomId = 1,
        )
    )
    fun getAllChatRooms(): List<ChatRoom> {
        return rooms
    }
}

class MessagesRepository {
    val listOfMessages = mutableListOf(
        MessageItem(
            id = 0,
            authorId = 1,
            body = "This is a message",
            dateSent = "00:00",
            dateDeleted = null,
            dateRead = null,
            dateReceived = "00:00",
            file = null,
            fileId = null,
            dateStarred = null,
            senderId = 0,
            senderName = "Lionel",
            messageType = 0,
            messageTag = null,
            messageTagId = null,
            reaction = null,
            linkedId = 0,
            receiverId = 1,
            chatRoomId = 0
        ),
        MessageItem(
            id = 1,
            authorId = 1,
            body = "This is a another message",
            dateSent = "00:00",
            dateDeleted = null,
            dateRead = null,
            dateReceived = "00:00",
            file = null,
            fileId = null,
            dateStarred = null,
            senderId = 0,
            senderName = "Lionel",
            messageType = 0,
            messageTag = null,
            messageTagId = null,
            reaction = null,
            linkedId = 0,
            receiverId = 1,
            chatRoomId = 0
        ),
        MessageItem(
            id = 2,
            authorId = 1,
            body = "This is a another message",
            dateSent = "00:00",
            dateDeleted = null,
            dateRead = null,
            dateReceived = "00:00",
            file = null,
            fileId = null,
            dateStarred = null,
            senderId = 0,
            senderName = "Lionel",
            messageType = 0,
            messageTag = null,
            messageTagId = null,
            reaction = null,
            linkedId = 0,
            receiverId = 1,
            chatRoomId = 1
        ),
        MessageItem(
            id = 3,
            authorId = 1,
            body = "This is a another message",
            dateSent = "00:00",
            dateDeleted = null,
            dateRead = null,
            dateReceived = "00:00",
            file = null,
            fileId = null,
            dateStarred = null,
            senderId = 0,
            senderName = "Lionel",
            messageType = 0,
            messageTag = null,
            messageTagId = null,
            reaction = null,
            linkedId = 0,
            receiverId = 1,
            chatRoomId = 1
        )
    )

    fun getAllMessages(chatRoomId: Int): List<MessageItem> {
        return listOfMessages.filter { it.chatRoomId==chatRoomId }
    }
    fun postMessage(body: String){
        val message = MessageItem(
            id = 0,
            authorId = 1,
            body = body,
            dateSent = "00:00",
            dateDeleted = null,
            dateRead = null,
            dateReceived = "00:00",
            file = null,
            fileId = null,
            dateStarred = null,
            senderId = 0,
            senderName = "Lionel",
            messageType = 0,
            messageTag = null,
            messageTagId = null,
            reaction = null,
            linkedId = 0,
            receiverId = 1,
            chatRoomId = 0
        )

        listOfMessages.add(message)
    }
}
