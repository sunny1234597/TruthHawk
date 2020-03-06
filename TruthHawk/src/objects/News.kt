package objects

/*
    TruthHawk - 차대연(차이나게이트 대응연합) 조선족 댓글공작 감시체계(네이버용)
*/

class News(newsURL:String, newsTitle:String, newsReaction:Int, Comments:Array<Comment>){
    val newsURL = newsURL
    val newsTitle = newsTitle
    val newsReaction = newsReaction
    val Comments = Comments
}