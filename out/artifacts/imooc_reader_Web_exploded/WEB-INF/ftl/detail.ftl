<!DOCTYPE html>
<html lang="en">
<head>
    <#-- 52.更改detail.ftl将静态页面改成动态页面 <title> 还有下面图书细节 -->
    <#--    53下面描述书籍详情的整段字段变成动态-->
    <meta charset="UTF-8">
    <title>${book.bookName}</title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <meta name="referrer" content="no-referrer">
    <link rel="stylesheet" href="/resources/bootstrap/bootstrap.css">
    <link rel="stylesheet" href="/resources/raty/lib/jquery.raty.css">
    <script src="/resources/jquery.3.3.1.min.js"></script>
    <script src="/resources/bootstrap/bootstrap.min.js"></script>
    <script src="/resources/art-template.js"></script>
    <script src="/resources/raty/lib/jquery.raty.js"></script>
    <style>
        .container {
            padding: 0px;
            margin: 0px;
        }

        .row {
            padding: 0px;
            margin: 0px;
        }

        .alert {
            padding-left: 0.5rem;
            padding-right: 0.5rem;
        }

        .col- * {
            padding: 0px;
        }

        .description p {
            text-indent: 2em;
        }

        .description img {
            width: 100%;
        }

        .highlight {
            background-color: lightskyblue !important;
        }

    </style>
    <script>
        $.fn.raty.defaults.path = '/resources/raty/lib/images';
        $(function () {
            $(".stars").raty({readOnly: true});
        })
        // 80。想看与看过 freemark脚本 如果它存在(想看/看过) 81产生对应的状态数据 MemberService
        $(function () {
            <#if memberReadState ??>
            // 重选阅读状态回填
            $("*[data-read-state='${memberReadState.readState}']").addClass("highlight");
            </#if>
            <#if !loginMember ??>
            $("*[data-read-state],#btnEvaluation,*[data-evaluation-id]").click(function () {
                // 利用jquery选择div的对话框函数 显示需要登录
                $("#exampleModalCenter").modal("show");
            })
            </#if>

            //82 完成登录状态  83下方实现写短评功能
            <#if loginMember ??>
            /**
             * 更新会员阅读状态
             */
            $("*[data-read-state]").click(function () {
                //会员阅读状态
                var readState = $(this).data("read-state");
                //发送请求
                $.post("/update_read_state", {
                    memberId: ${loginMember.memberId},
                    bookId: ${book.bookId},
                    readState: readState
                }, function (json) {
                    if (json.code == "0") { //服务器处理成功
                        $("*[data-read-state]").removeClass("highlight");//高亮的清除
                        $("*[data-read-state='" + readState + "']").addClass("highlight");//状态值放入其中
                    }
                }, "json")
            });
            //83.短评功能  下面短评联动现象
            $("#btnEvaluation").click(function () {
                // 选中id＝score的标签 转换为星型组件
                $("#score").raty({});
                $("#dlgEvaluation").modal("show");//显示短评对话框
            })
            //86.评论对话框提交数据  87完成点赞核心实现 MemberService
            $("#btnSubmit").click(function () {
                var score = $("#score").raty("score");//获取评分
                var content = $("#content").val();
                if (score == 0 || $.trim(content) == ""){ //没有进行选择 或 删除前后空格
                    return; //禁止提交方法中断
                }
                $.post("/evaluate",{
                    score : score,
                    bookId: ${book.bookId},
                    memberId: ${loginMember.memberId},
                    content: content
                },function (json) {
                    if (json.code == "0") {//处理成功
                        window.location.reload();//列表进行刷新
                    }
                },"json")
            })
            // 88.评论点赞  89图书评分自动计算book.xml
            $("*[data-evaluation-id]").click(function(){
                var evaluationId = $(this).data("evaluation-id");
                $.post("/enjoy",{evaluationId:evaluationId},function(json){
                    if(json.code == "0"){
                        $("*[data-evaluation-id='" + evaluationId + "'] span").text(json.evaluation.enjoy);
                    }
                },"json")
            })
            </#if>
        })
    </script>
</head>
<body>
<!--<div style="width: 375px;margin-left: auto;margin-right: auto;">-->
<div class="container ">
    <nav class="navbar navbar-light bg-white shadow mr-auto">
        <ul class="nav">
            <li class="nav-item">
                <a href="/">
                    <img src="https://m.imooc.com/static/wap/static/common/img/logo2.png" class="mt-1"
                         style="width: 100px">
                </a>
            </li>

        </ul>
    </nav>


    <div class="container mt-2 p-2 m-0" style="background-color:rgb(127, 125, 121)">
        <div class="row">
            <div class="col-4 mb-2 pl-0 pr-0">
                <img style="width: 110px;height: 160px"
                     src="${book.cover}">
            </div>
            <div class="col-8 pt-2 mb-2 pl-0">
                <h6 class="text-white">${book.bookName}</h6>
                <div class="p-1 alert alert-warning small" role="alert">
                    ${book.subTitle}
                </div>
                <p class="mb-1">
                    <span class="text-white-50 small">${book.author}</span>
                </p>
                <div class="row pl-1 pr-2">
                    <div class="col-6 p-1">
                        <button type="button" data-read-state="1" class="btn btn-light btn-sm w-100">
                            <img style="width: 1rem;" class="mr-1"
                                 src="https://img3.doubanio.com/f/talion/cf2ab22e9cbc28a2c43de53e39fce7fbc93131d1/pics/card/ic_mark_todo_s.png"/>想看
                        </button>
                    </div>
                    <div class="col-6 p-1">
                        <button type="button" data-read-state="2" class="btn btn-light btn-sm  w-100">
                            <img style="width: 1rem;" class="mr-1"
                                 src="https://img3.doubanio.com/f/talion/78fc5f5f93ba22451fd7ab36836006cb9cc476ea/pics/card/ic_mark_done_s.png"/>看过
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="row" style="background-color: rgba(0,0,0,0.1);">
            <div class="col-2"><h2 class="text-white">${book.evaluationScore}</h2></div>
            <div class="col-5 pt-2">
                <span class="stars" data-score="${book.evaluationScore}"></span>
            </div>
            <div class="col-5  pt-2"><h5 class="text-white">${book.evaluationQuantity}</h5></div>
        </div>
    </div>
    <div class="row p-2 description">
        <#--  53.来源于数据底层的description描述字段   54显示动态评论列表 数据库有evaluation表 创建一个entity/Evaluation-->
        ${book.description}
    </div>

    <div class="alert alert-primary w-100 mt-2" role="alert">短评
        <button type="button" id="btnEvaluation" class="btn btn-success btn-sm text-white float-right"
                style="margin-top: -3px;">
            写短评
        </button>
    </div>
    <div class="reply pl-2 pr-2">
        <#--  57.对短评进行动态页面的整合与梳理 进行循环遍历  58注意下面的慕粉-126对应着数据库的会员表 再重来一遍 Evaluation-->
        <#list evaluationList as evaluation>
            <div>
                <div>
                    <span class="pt-1 small text-black-50 mr-2">${evaluation.createTime?string('MM-dd')}</span>
                    <#--   60.修改 慕粉-126 变为动态的   61Kaptcha验证码的实现去pom增加依赖-->
                    <span class="mr-2 small pt-1">${evaluation.member.nickname}</span>
                    <span class="stars mr-2" data-score="${evaluation.score}"></span>

                    <button type="button" data-evaluation-id="${evaluation.evaluationId}"
                            class="btn btn-success btn-sm text-white float-right" style="margin-top: -3px;">
                        <img style="width: 24px;margin-top: -5px;" class="mr-1"
                             src="https://img3.doubanio.com/f/talion/7a0756b3b6e67b59ea88653bc0cfa14f61ff219d/pics/card/ic_like_gray.svg"/>
                        <span>${evaluation.enjoy}</span>
                    </button>
                </div>

                <div class="row mt-2 small mb-3">
                    ${evaluation.content}
                </div>
                <hr/>
            </div>
        </#list>
    </div>
</div>


<!-- Modal -->
<div class="modal fade" id="exampleModalCenter" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body">
                您需要登录才可以操作哦~
            </div>
            <div class="modal-footer">
                <a href="/login.html" type="button" class="btn btn-primary">去登录</a>
            </div>
        </div>
    </div>
</div>

<!-- Modal -->
<div class="modal fade" id="dlgEvaluation" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body">
<#-- 83. 短评联动   84 MemberService.java 短评实现代码 -->
                <h6>为${book.bookName}写短评</h6>
                <form id="frmEvaluation">
                    <div class="input-group  mt-2 ">
                        <span id="score"></span>
                    </div>
                    <div class="input-group  mt-2 ">
                        <input type="text" id="content" name="content" class="form-control p-4"
                               placeholder="这里输入短评">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" id="btnSubmit" class="btn btn-primary">提交</button>
            </div>
        </div>
    </div>
</div>

</body>
</html>