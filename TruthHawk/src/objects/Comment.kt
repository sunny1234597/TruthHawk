package objects
import java.util.*

/*
    TruthHawk - 차대연(차이나게이트 대응연합) 조선족 댓글공작 감시체계
*/

//작성자 ID, 내용, 댓글 종류(댓글, 대댓글), 작성시간, 좋아요 수, 싫어요 수, 대댓글 리스트
class Comment(ID:String, Content:String, WrittenTime:String, NewsTitle:String, URL:String, HandUp:Int= 0, HandDown:Int = 0){
    val ID = ID
    val Content = Content
    val WrittenTime = WrittenTime
    val NewsTitle = NewsTitle
    val URL = URL
    val HandUp = HandUp
    val HandDown = HandDown
}

//댓글 종류 enum
enum class CommentType(){
    Comment,
    ReplyToComment
}