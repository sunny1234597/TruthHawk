/*
    TruthHawk - 차대연(차이나게이트 대응연합) 조선족 댓글공작 감시체계(네이버용)
*/

import objects.Comment
import objects.CommentType
import objects.News
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.net.URLEncoder

fun main(args:Array<String>){
    //크롬드라이버 경로로 설정
    System.setProperty("webdriver.chrome.driver", "libs\\driver\\chromedriver.exe")
    var Client = ChromeDriver(ChromeOptions().addArguments("--incognito")) //시크릿모드로 실행

    //모니터링할 뉴스들의 키워드
    var Keywords = arrayOf<String>(
        /*"문재인",
        "코로나",
        "조선족",
        "중국",
        "차이나게이트",*/
        "차이나게이트 방지법"
    )

    //감시할 뉴스 링크들(추후 자동으로 링크를 수집함)
    var newsLinks = mutableListOf<String>()

    //뉴스 추가
    var Targets = mutableListOf<String>()
    Keywords.forEach{
        Client.get("https://search.naver.com/search.naver?where=news&query=${URLEncoder.encode(it, "UTF-8")}")
        WebDriverWait(Client, 20).until(ExpectedConditions.visibilityOfElementLocated(By.className("_sp_each_url"))) //기사 로딩까지 대기
        Client.findElements(By.className("_sp_each_url")).forEach(){
            if(it.text == "네이버뉴스"){ //파싱이 가능한 네이버뉴스만 수집
                Targets.add(it.getAttribute("href"))
            }
        }
    }
    Targets.forEach{
        try{
            Client.get(it) //댓글 창 url을 수집하기 위해 기사로 이동
            WebDriverWait(Client, 5).until(ExpectedConditions.visibilityOfElementLocated(By.className("u_cbox_btn_view_comment"))) //기사 로딩까지 대기
            Client.findElement(By.className("u_cbox_btn_view_comment")).click()
            //WebDriverWait(Client, 2).until(ExpectedConditions.visibilityOfElementLocated(By.className("cbox_module"))) //속도 저하 요소로 판단, 일시로 비활성화함(문제는 발생하지 않음)
            if(!newsLinks.contains(Client.currentUrl)) //겹치는 URl이 아니라면
                newsLinks.add(Client.currentUrl)
            println(it)
        }catch (ex:Exception){}
    }

    var ParsedNewsList = mutableListOf<News>() //기사들
    newsLinks.forEach{
        var Comments = mutableListOf<Comment>() //기사 당 댓글 리스트
        //댓글 수집 단계
        Client.get(it)
        WebDriverWait(Client, 20).until(ExpectedConditions.visibilityOfElementLocated(By.className("u_cbox_comment_box"))) //로딩까지 대기
        while(existElement(Client, "u_cbox_btn_more") && iscanDisyplay(Client, "u_cbox_paginate")){ //더이상 불러올 댓글이 없을 때까지 로딩
            try{
                Client.findElementByClassName("u_cbox_btn_more").click()
            }catch (ex:Exception){}
        }
        var NewsDoc:Document = Jsoup.parse(Client.pageSource)
        var NewsCommentsDoc = NewsDoc.select(".u_cbox_comment_box")

        //댓글 전처리(객체화) 단계
        NewsCommentsDoc.forEach(){
            try {
                var beforeObject = Comment(
                    it.select(".u_cbox_nick").text(),
                    it.select(".u_cbox_contents").text(),
                    it.select(".u_cbox_date").text(),
                    NewsDoc.select("title")[0].text(),
                    Client.currentUrl,
                    it.select(".u_cbox_cnt_recomm").text().toInt(), it.select(".u_cbox_cnt_unrecomm").text().toInt())
                Comments.add(beforeObject)
            }
            catch(ex:Exception){}
        }
        Client.get(Client.findElement(By.className("media_end_head_btn_type")).getAttribute("href"))
        WebDriverWait(Client, 20).until(ExpectedConditions.visibilityOfElementLocated(By.className("u_likeit_text"))) //반응 수 확인을 위해 본 기사 로딩까지 대기
        var NewsReactionCount =  Client.findElement(By.cssSelector(".u_likeit_text._count.num")).text.replace(",", "").toInt()
        ParsedNewsList.add(News(Client.currentUrl, NewsDoc.select("title")[0].text(), NewsReactionCount, Comments.toTypedArray()))
    }

    //댓글 분석 단계
    println("총 ${ParsedNewsList.size}개의 기사 발견\n\n")
    ParsedNewsList.forEach{
        println("  ${it.newsTitle} | URL : ${it.newsURL} | 댓글 : ${it.Comments.size}개 | 반응수 : ${it.newsReaction}개")
        it.Comments.forEach {
            println("\n    ${it.ID} | ${it.WrittenTime} | ${it.NewsTitle}(${it.URL})\n        ${it.Content}\n        좋아요 : ${it.HandUp} / 싫어요 : ${it.HandDown}")
        }
    }

}

//Element가 존재하는지 확인
fun existElement(Client:WebDriver, className:String):Boolean{
    try{
        Client.findElement(By.className(className))
        return true
    }
    catch(ex:Exception){
        return false
    }
}

//보이는 element인지 확인
fun iscanDisyplay(Client:WebDriver, className:String):Boolean{
    try{
        var element = Client.findElement(By.className(className))
        if(element.isDisplayed){
            return true
        }else{
            return false
        }
    }
    catch(ex:Exception){
        return false
    }
}