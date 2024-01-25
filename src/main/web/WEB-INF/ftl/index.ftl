<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="referrer" content="no-referrer">
    <title>慕课书评网</title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <link rel="stylesheet" href="/resources/bootstrap/bootstrap.css">
    <link rel="stylesheet" href="/resources/raty/lib/jquery.raty.css">
    <script src="/resources/jquery.3.3.1.min.js"></script>
    <script src="/resources/bootstrap/bootstrap.min.js"></script>
    <script src="/resources/art-template.js"></script>
    <script src="/resources/raty/lib/jquery.raty.js"></script>
    <style>
        .highlight {
            color: red !important;
        }

        a:active {
            text-decoration: none !important;
        }
    </style>


    <style>
        .container {
            padding: 0px;
            margin: 0px;
        }

        .row {
            padding: 0px;
            margin: 0px;
        }

        .col- * {
            padding: 0px;
        }
    </style>

    <#--39.type="text/html"说明当前script块中包含的内容是一段一段的html浏览器不会作为javascript进行解析-->
    <#-- ★ ★ ★ id是模板名字 两组大括号进行提取数据 ★ ★ ★ 使用模板引擎简化产生html过程-->
    <#--    40去修改下方script代码导入的形式-->
    <script type="text/html" id="tpl">
        <a href="/book/{{bookId}}" style="color: inherit">
            <div class="row mt-2 book">
                <div class="col-4 mb-2 pr-2">
                    <img class="img-fluid" src="{{cover}}">
                </div>
                <div class="col-8  mb-2 pl-0">
                    <h5 class="text-truncate">{{bookName}}</h5>

                    <div class="mb-2 bg-light small  p-2 w-100 text-truncate">{{author}}</div>


                    <div class="mb-2 w-100">{{subTitle}}</div>

                    <p>
                        <span class="stars" data-score="{{evaluationScore}}" title="gorgeous"></span>
                        <#--                        <img alt="1" 加入了星型组件免去这些操作 -->
                        <#--                             src="./resources/raty/lib/images/star-on.png"-->
                        <#--                             title="gorgeous">&nbsp;<img alt="2"-->
                        <#--                                                         src="./resources/raty/lib/images/star-on.png"-->
                        <#--                                                         title="gorgeous">&nbsp;<img-->
                        <#--                                alt="3" src="./resources/raty/lib/images/star-on.png" title="gorgeous">&nbsp;<img-->
                        <#--                                alt="4" src="./resources/raty/lib/images/star-on.png" title="gorgeous">&nbsp;<img-->
                        <#--                                alt="5" src="./resources/raty/lib/images/star-on.png" title="gorgeous"><input-->
                        <#--                                name="score" type="hidden" value="{{evaluationScore}}" readonly=""></span>-->
                        <span class="mt-2 ml-2">{{evaluationScore}}</span>
                        <span class="mt-2 ml-2">{{evaluationQuantity}}人已评</span>
                    </p>
                </div>
            </div>
        </a>
        <hr>
    </script>
    <#--    38.编写Json序列化格式并追加到当前网页 上方以导入Art-Template模板引擎 39将<div id="bookList">下面的代码裁切放到上面-->
    <script>
        <#-- 41.引入星星图片 和 评价方法 上面的script运用简便写法 42下面 $(".stars")将星星的span标签选中 raty转换成可视的星星组件(被注释)-->
        $.fn.raty.defaults.path = "/resources/raty/lib/images"
        // 45.对两次Ajax的代码进行重构与梳理(定义一个loadMore()方法将下面的裁切进来底下写进递归)
        // isReset参数设置true,代表从第一页开始查询,否则按nextPage查询后续页
        // 对下面那个data: {p: 1}, 进行重构 ↓↓↓↓ isReset 下面代码已经被注释但未完全删除
        // loadMore()加载更多数据 46下方整理点击时的高亮显示 [全部]
        function loadMore(isReset){
            if(isReset == true){
                $("#bookList").html("");
                $("#nextPage").val(1);
            }
            // 50.让Ajax获取数据在下方data填写 下方$(".category和.order")都要重新调用递归
            // 细节清空再显示 上方$("#bookList").html(""); 51开发图书详情页
            // 51导入训练素材 的详情页.html => detail.ftl并添加BookService
            var nextPage = $("#nextPage").val();
            var categoryId= $("#categoryId").val();
            var order = $("#order").val();
            //接下来就是发送数据时 组织成参数发送服务器
            $.ajax({
                url : "/books" ,
                //nextPage为2可以加载第二页  ↓字符串：变量
                data : {p:nextPage,"categoryId":categoryId , "order":order},
                type : "get" ,
                dataType : "json" ,
                success : function(json){
                    console.info(json);
                    var list = json.records;
                    for(var i = 0 ; i < list.length ; i++){
                        var book = json.records[i];
                        // var html = "<li>" + book.bookName + "</li>";
                        //将数据结合tpl模板,生成html
                        var html = template("tpl" , book);
                        console.info(html);
                        //jquery的id选择器选中div对象 追加
                        $("#bookList").append(html);
                    }
                    $(".stars").raty({readOnly:true});
                    // 44.如果当前页小于总页数 下面利用val设置隐藏域的值
                    // 可能将按照字符串处理 结果是31而不是4 要强制转换
                    // 若有后续数据的话 最后几行的divNoMore需要隐藏起来
                    // 45对两次的Ajax代码进行重构与梳理 上面找到第一个script
                    if(json.current < json.pages){
                        $("#nextPage").val(parseInt(json.current) + 1);
                        $("#btnMore").show();
                        $("#divNoMore").hide();
                    }else{
                        $("#btnMore").hide();
                        $("#divNoMore").show();
                    }
                }
            })
        }

        $(function () {
            // $.ajax({ 上面已经对次代码进行重构与梳理
            //     url: "/books",
            //     data: {p: 1},
            //     type: "get",
            //     dataType: "json",
            //     success: function (json) {
            //         console.info(json);
            //         var list = json.records;//获取当前分页数据
            //         for (var i = 0; i < list.length; i++) {
            //             var book = json.records[i];
            //             // 动态将数据组合成html
            //             // var html = "<li>" + book.bookName + "</li>";
            //             // 40.替换导入数据形式 (传入模板id,传入数据) 将数据结合tpl模板,生成html
            //             // 41当评分降低的时候顺带着星星的亮度降低 raty星型评分组件 上面已准备好raty css js引入
            //             // 41在上面几行引入星型评分的script方法
            //             var html = template("tpl", book);
            //             console.info(html);
            //             //jquery的id选择器选中div对象 追加
            //             $("#bookList").append(html);
            //         }
            //         //42.显示星型评价组件 只读并不能修改  43修改页码分页查询 下一个$function()
            //         $(".stars").raty({readonly: true});
            //     }
            // })
            loadMore(true);
        })


        // 43.用于绑定加载更多按钮单击事件 44上面的设置页面的逻辑及处理
        $(function(){
            $("#btnMore").click(function(){
                loadMore();
            })
            //46 增加点击的显示控件
            $(".category").click(function () {
                $(".category").removeClass("highlight"); //移除高亮
                $(".category").addClass("text-black-50");//增添灰色
                $(this).addClass("highlight");//增加高亮
                //49添加数量与热度 ↓点击的超链接 在下面也要添加order
                var categoryId = $(this).data("category"); //214行定义了data-category
                $("#categoryId").val(categoryId);
                loadMore(true); //每次点完要重新查询
            })//在排序处显示控件 47点击 全部|前端|后端 产生数据联动BookService上

            $(".order").click(function(){
                $(".order").removeClass("highlight"); //移除高亮
                $(".order").addClass("text-black-50");//增添灰色
                $(this).addClass("highlight");//增加高亮
                //~49.提取设置到隐藏域中 发送请求到Ajax服务器上最上面 50var categoryId
                var order = $(this).data("order");
                $("#order").val(order); //点击不同隐藏域时为其赋值
                loadMore(true);
            })
        })
    </script>
</head>
<body>
<div class="container">
    <nav class="navbar navbar-light bg-white shadow mr-auto">
        <ul class="nav">
            <li class="nav-item">
                <a href="/">
                    <img src="https://m.imooc.com/static/wap/static/common/img/logo2.png" class="mt-1"
                         style="width: 100px">
                </a>
            </li>

        </ul>
  <#--   77.将获取到的member的session数据替换成登录右上角的动态数据-->
  <#--  两个问号代表前面的属性是存在的情况下输出其中的html 78新增entity/MemberReadState.java会员阅读状态-->
        <#if loginMember??>
            <h6 class="mt-1">
                <img style="width: 2rem;margin-top: -5px" class="mr-1" src="./images/user_icon.png">${loginMember.nickname}
            </h6>
            <#else> <#--上面是已登录 下面是未登录-->
                <a href="/login.html" class="btn btn-light btn-sm">
                    <img style="width: 2rem;margin-top: -5px" class="mr-1" src="./images/user_icon.png">登录
                </a>
        </#if>

    </nav>
    <div class="row mt-2">


        <div class="col-8 mt-2">
            <h4>热评好书推荐</h4>
        </div>
        <#--在这块进行动态数据编写 24.创建entity/Category分类实体-->
        <div class="col-8 mt-2">
            <#--   46.整顿全部高亮显示 整顿下面按热度的高亮显示 上方加入script-->
            <span data-category="-1" style="cursor: pointer" class="highlight  font-weight-bold category">全部</span>
            |
            <#--     31.对页面进行动态的数据导入    32创建全新实体类Book   -->
            <#list categoryList as category>
                <a style="cursor: pointer" data-category="${category.categoryId}"
                   class="text-black-50 font-weight-bold category">${category.categoryName}</a>
            <#--                取消最后一个的竖线-->
                <#if category_has_next>|</#if>
            </#list>
        </div>

        <div class="col-8 mt-2">
            <span data-order="quantity" style="cursor: pointer"
                  class="order highlight  font-weight-bold mr-3">按热度</span>

            <span data-order="score" style="cursor: pointer"
                  class="order text-black-50 mr-3 font-weight-bold">按评分</span>
        </div>
    </div>
    <div class="d-none">
        <input type="hidden" id="nextPage" value="2">
        <input type="hidden" id="categoryId" value="-1">
        <input type="hidden" id="order" value="quantity">
    </div>
    <#--循环产生一块一块的超链接 38去36行增加一个script 需要发送ajax和BookController的books进行交互[需要jquery.3.3.1.min.js]-->
    <div id="bookList"> <#--39.裁切下面的代码 去39行写script块-->

    </div>
    <button type="button" id="btnMore" data-next-page="1" class="mb-5 btn btn-outline-primary btn-lg btn-block">
        点击加载更多...
    </button>
    <div id="divNoMore" class="text-center text-black-50 mb-5" style="display: none;">没有其他数据了</div>
</div>

</body>
</html>