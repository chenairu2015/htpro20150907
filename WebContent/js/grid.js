(function($) {

	$.addGrid = function(t, p) {
		if (t.grid){
			return false; // return if already exist
		}
		// apply default properties
		p = $.extend({
			name : t.id,								//表格名称
			property : "",								//上下文属性
			label : false,								//标题
			dataUrl : false,							//数据加载地址
			autoInit : true,							//是否自动初始化
			autoLoad : false,							//是否自动加载数据
			preProcess : false,							//数据填充grid前预处理
			hide : false,								//初始化时隐藏
			modelUrl : false,							//构建模型加载地址
			structureModel : false,						//构建模型
			allowSubmitData : true,						//表单提交时是否提交grid数据
			
			width : 'auto', 							//表格宽度，auto表示根据每列的宽度自动计算
			height : 'adapt', 							//表格高度,默认自适应
			
			allowResize : false,						//是否可伸缩
			allowMinisize : false,						//是否可以最小化
			allowMoveHead : false,						//是否允许拖动表格头部(表头移动列顺序随之变化)
			allowHideColumn : true,						//是否允许显示/隐藏列
			allowClickSelect : false,					//是否允许单击(ctrl,shift)选择
			isSingleSelect : true,						//是否单击只能选择一行否则可选择多行
			
			showLineStripe : true,						//显示行斑纹效果，默认是奇偶交互的形式
			showLineNum : true,							//显示行号
			showPage : true,							//显示分页
			pageSize : 15,								//默认每页条数
			showPageSizeOptions : true,					//是否可以动态设置每页显示的结果数
			pageSizeOptions : '15,30,50',				//可选择设定的每页结果数
			showExportExcel : false,					//显示导出excel
			showExportDbf : false,						//显示导出dbf
			exportFileName : "file",					//导出文件名
			
			onTrClick : false,							//行单击事件
			onTrDblclick : false,						//行双击事件
			onChangePage : false,						//翻页事件
			onInit : false,								//初始化完毕(grid渲染完成后执行,数据不一定加载完)
			onSuccess : false,							//数据加载成功
			onSubmit : false,							//数据提交前
			
			isAjax : true,								//是否ajax方式加载数据
			novstripe : false,							//是否不显示表格内横竖线，默认显示
			minwidth : 30, 								//列的最小宽度
			minheight : 80, 							//列的最小高度
			dataType : 'json', 							//数据加载的类型
			total : 0, 									//总数据量
			page : 1, 									//默认当前页
			rowIdName: false,							//行id索引属性
			minColToggle : 1, 							//允许显示的最小列数
			hideOnSubmit : true,						//隐藏提交
			blockOpacity : 0.5,							//透明度设置
			onChangeSort : false,						//当改变排序时(可在此方法中重写默认实现，自行实现客户端排序)
			form:null,									//所在form
			model:{"th":[],"code":[]},					//导出模型
			gridData:"_grid_data_"+t.id,				//数据
			gridParams:"_grid_params_"+t.id,			//参数name(名字)page(当前页号)pageSize(显示条数)expType(导出类型)expModel(导出模型)
			expType:{"EXCEL":"EXCEL","DBF":"DBF"}		//导出文件类型
		  }, p);
		// show if hidden
		// remove padding and spacing,width properties
		$(t).show().attr({cellPadding : 0,cellSpacing : 0,border : 0})
			.removeAttr('width');
		// create grid class
		var g = {
			hset : {},
			rePosDrag : function() {
				var cdleft = 0 - this.hDiv.attr("scrollLeft");
				if (this.hDiv.attr("scrollLeft") > 0){
					cdleft -= Math.floor(p.cgwidth / 2);
				}
				g.cDrag.css({
				    top : g.hDiv.attr("offsetTop") + 1
			    });
				var cdpad = this.cdpad;

				$('div', g.cDrag).hide();
				$('thead tr:first th:visible', this.hDiv).each(function() {
				    var n = $('thead tr:first th:visible', g.hDiv).index(this);
				    var cdpos = parseInt($('div', this).width());
				    var ppos = cdpos;
				    if (cdleft == 0){
				    	cdleft -= Math.floor(p.cgwidth / 2);
				    }
				    cdpos = cdpos + cdleft + cdpad;
				    $('div:eq(' + n + ')', g.cDrag).css({
				        'left' : cdpos + 'px'
			        }).show();
				    cdleft = cdpos;
			    });
			},
			fixHeight : function(newH) {
				if (!newH){
					newH = g.bDiv.height();
				}
				var hdHeight = this.hDiv.height();
				$('div', this.cDrag).each(function() {
				    $(this).height(newH + hdHeight);
			    });
				var nd = parseInt(g.nDiv.height());
				if (nd > newH){
					g.nDiv.height(newH).width(200);
				}else{
					g.nDiv.height('auto').width('auto');
				}
				g.block.css({
				    height : newH,
				    marginBottom : (newH * -1)
			    });
				var hrH = g.bDiv.attr("offsetTop") + newH;
				if (p.height != 'auto' && p.allowResize){
					hrH = g.vDiv.attr("offsetTop");
				}
				g.rDiv.css({height : hrH});
			},
			dragStart : function(dragtype, e, obj) { // default drag function start
				if (dragtype == 'colresize') // column resize
				{
					g.nDiv.hide();
					g.nBtn.hide();
					var n = $('div', this.cDrag).index(obj);
					var ow = $('th:visible div:eq(' + n + ')', this.hDiv).width();
					$(obj).addClass('dragging').siblings().hide();
					$(obj).prev().addClass('dragging').show();

					this.colresize = {
						startX : e.pageX,
						ol : parseInt(obj.style.left),
						ow : ow,
						n : n
					};
					$('body').css('cursor', 'col-resize');
				} else if (dragtype == 'vresize') // table resize
				{
					var hgo = false;
					$('body').css('cursor', 'row-resize');
					if (obj) {
						hgo = true;
						$('body').css('cursor', 'col-resize');
					}
					this.vresize = {
						h : g.bDiv.height(),
						sy : e.pageY,
						w : p.width,
						sx : e.pageX,
						hgo : hgo
					};
				}
				else if (dragtype == 'colMove') // column header drag
				{
					g.nDiv.hide();
					g.nBtn.hide();
					this.hset = this.hDiv.offset();
					this.hset.right = this.hset.left + $('table', this.hDiv).width();
					this.hset.bottom = this.hset.top + $('table', this.hDiv).height();
					this.dcol = obj;
					this.dcoln = $('th', this.hDiv).index(obj);

					this.colCopy = $("<div/>").addClas("colCopy").html(obj.innerHTML);
					if ($.browser.msie) {
						this.colCopy.addClass("ie");
					}
					this.colCopy.css({
						    position : 'absolute',
						    float : 'left',
						    display : 'none',
						    textAlign : obj.align
					    });
					$('body').append(this.colCopy);
					$(this.cDrag).hide();
				}
				$('body').noSelect();
			},
			dragMove : function(e) {
				if (this.colresize) // column resize
				{
					var n = this.colresize.n;
					var diff = e.pageX - this.colresize.startX;
					var nleft = this.colresize.ol + diff;
					var nw = this.colresize.ow + diff;
					if (nw > p.minwidth) {
						$('div:eq(' + n + ')', this.cDrag).css('left', nleft);
						this.colresize.nw = nw;
					}
				} else if (this.vresize) // table resize
				{
					var v = this.vresize;
					var y = e.pageY;
					var diff = y - v.sy;
					if (!p.defwidth){
						p.defwidth = p.width;
					}
					if (p.width != 'auto' && !p.nohresize && v.hgo) {
						var x = e.pageX;
						var xdiff = x - v.sx;
						var newW = v.w + xdiff;
						if (newW > p.defwidth) {
							this.gDiv.width(newW);
							p.width = newW;
						}
					}
					var newH = v.h + diff;
					if ((newH > p.minheight || p.height < p.minheight) && !v.hgo) {
						this.bDiv.height(newH);
						p.height = newH;
						this.fixHeight(newH);
					}
					v = null;
				} else if (this.colCopy) {
					$(this.dcol).addClass('thMove').removeClass('thOver');
					if (e.pageX > this.hset.right || e.pageX < this.hset.left
					    || e.pageY > this.hset.bottom || e.pageY < this.hset.top) {
						// this.dragEnd();
						//$('body').css('cursor', 'move');
						$('body').css('cursor', 'not-allowed');
					} else{
						$('body').css('cursor', 'pointer');
					}
					this.colCopy.css({
					    top : e.pageY + 5,
					    left : e.pageX + 5,
					    display : 'block'
				    });
				}
			},
			dragEnd : function() {
				if (this.colresize) {
					var n = this.colresize.n;
					var nw = this.colresize.nw;

					$('th:visible div:eq(' + n + ')', this.hDiv).css('width', nw);
					$('tr', this.bDiv).each(function() {
					    $('td:visible div:eq(' + n + ')', this).css('width', nw);
				    });
					this.hDiv.attr("scrollLeft", this.bDiv.attr("scrollLeft"));

					$('div:eq(' + n + ')', this.cDrag).siblings().show();
					$('.dragging', this.cDrag).removeClass('dragging');
					this.rePosDrag();
					this.fixHeight();
					this.colresize = false;
				} else if (this.vresize) {
					this.vresize = false;
				} else if (this.colCopy) {
					this.colCopy.remove();
					if (this.dcolt != null) {
						if (this.dcoln > this.dcolt){
							$('th:eq(' + this.dcolt + ')', this.hDiv).before(this.dcol);
						}else{
							$('th:eq(' + this.dcolt + ')', this.hDiv).after(this.dcol);
						}
						this.switchCol(this.dcoln, this.dcolt);
						$(this.cdropleft).remove();
						$(this.cdropright).remove();
						this.rePosDrag();
					}

					this.dcol = null;
					this.hset = null;
					this.dcoln = null;
					this.dcolt = null;
					this.colCopy = null;

					$('.thMove', this.hDiv).removeClass('thMove');
					this.cDrag.show();
				}
				$('body').css('cursor', 'default');
				$('body').noSelect(false);
			},
			switchCol : function(cdrag, cdrop) { // switch columns
				$('tbody tr', t).each(function() {
					if (cdrag > cdrop){
						$('td:eq(' + cdrop + ')', this).before($('td:eq(' + cdrag + ')',this));
					}else{
						$('td:eq(' + cdrop + ')', this).after($('td:eq(' + cdrag + ')',this));
					}
				});
				// switch order in nDiv
				if (cdrag > cdrop){
					$('tr:eq(' + cdrop + ')', this.nDiv).before($('tr:eq(' + cdrag + ')',
						    this.nDiv));
				}else{
					$('tr:eq(' + cdrop + ')', this.nDiv).after($('tr:eq(' + cdrag + ')',
						    this.nDiv));
				}
				if ($.browser.msie && $.browser.version < 7.0){
					$('tr:eq(' + cdrop + ') input', this.nDiv)[0].checked = true;
				}
				this.hDiv.attr("scrollLeft", this.bDiv.attr("scrollLeft"));
			},
			scroll : function() {
				this.hDiv.attr("scrollLeft",this.bDiv.attr("scrollLeft"));
				this.rePosDrag();
			},
			addRow : function(cell,id,index){
				var tr = $("<tr>");
			    if (p.showLineStripe && p.total%2){
			    	tr.addClass("erow");
			    }
			    p.total=p.total + 1;
			    // add cell
			    cell=cell==null?{}:cell;
			    var code,isArray=$.isArray(cell),jump=0;
			    var len = p.model.th.length;
			    for(var i=0;i<len;i++){
			    	code=p.model.code[i];
			    	var td = $("<td>");
			    	if(code.type=="seq"){
			    		td.html(p.total);
			    		jump++;
		        	}else{
		        		var value="";
		        		if(isArray){
		        			value=cell[i-jump];
		        		}else{
		        			var valueKey=code.property;
		        			if(code.property==""&&code.replace=="true"){
		        				valueKey=code.name.split("\.")[1];
							}
		        			value=cell[valueKey];
		        			if(p.rowIdName&&id==null&&p.rowIdName==code.name){
		        				id=value;
		        			}
		        		}
	    				td.html(value==null?"":value);
		        	}
			        tr.append(td);
			    }
			    //set row id
			    if(id!=null){
		    		tr.attr("id",id);
		    	}
			    
			    if(index==null){
			    	$(t).children("tbody").append(tr);
			    }else if((typeof index)=="number"){
			    	$(t).children("tbody").children("tr:eq("+index+")").after(tr);
			    }else if((typeof index)=="string"){
			    	$("#"+index, t).after(tr);
			    }
			    //初始化这一行
			    this.addCellProp(tr);
			    this.addRowProp(tr);
			},
			insertRow: function(index,cell,id){
				this.addRow(cell,id,index);
				if (p.showLineStripe){
					$(t).children("tbody").children("tr").each(function(i){
						var tr=$(this).removeClass("erow");
						if(i%2){
							tr.addClass("erow");
						}
					});
			    }
			},
			addData : function(service,data) { // parse data
				if (p.preProcess){
					data = p.preProcess(data);
				}
				this.loading = false;

				if (p.dataType == 'json'){
					p.total = data.total;
				}
				if (p.total == 0) {
					$('tr, a, td, div', t).unbind();
					$("tbody",t).empty();
					p.pages = 1;
					p.page = 1;
					this.buildpager();
					if (p.onSuccess){
						p.onSuccess(service,data);
					}
					if (p.hideOnSubmit){
						$(g.block).remove();
					}
					this.hDiv.scrollLeft = this.bDiv.scrollLeft;
					if ($.browser.opera){
						$(t).css('visibility', 'visible');
					}
					return false;
				}
				p.page=data.page;
				p.pages = Math.ceil(p.total / p.pageSize);
				this.buildpager();
				
				// build new body
				var tbody = $('<tbody>');
				var thLen=p.model.th.length;
				if (p.dataType == 'json') {
					var len=data.rows.length;
					var row,code,property,value;
					for(var i=0;i<len;i++){
						row=data.rows[i];
					    var tr = $("<tr/>");
					    var rowId=null;
					    if (p.showLineStripe && i%2){
					    	tr.addClass("erow");
					    }
					    var tds="";
					    // add cell
					    for(var j=0;j<thLen;j++){
					    	code=p.model.code[j];
					    	var td = "<td>"
					    	if(code.type=="seq"){
					    		td+=(i+1);
					    	}else{
					    		if(code.property==""&&code.replace=="true"){
					    			property=code.name;
								}else{
									property=code.property;
								}
			        			value=row[property];
			        			if(p.rowIdName&&rowId==null&&p.rowIdName==code.name){
			        				rowId=value;
			        			}
					    		td+=(value==null?"":value);
					    	}
					    	td+="</td>";
					    	tds+=td;
					        td = null;
					    }
					    //set row id
					    if(rowId!=null){
					    	tr.attr("id",rowId);
					    }
					    tbody.append(tr.append(tds));
					    tr = null;
					}
				}
				$('tr', t).unbind();
				$("tbody",t).remove();
				$(t).append(tbody);
				this.addCellProp();
				this.addRowProp();
				this.rePosDrag();
				tbody = null;

				if (p.onSuccess){
					p.onSuccess(service,data);
				}
				data = null;
				if (p.hideOnSubmit){
					$(g.block).remove();
				}
				this.hDiv.scrollLeft = this.bDiv.scrollLeft;
				if ($.browser.opera){
					$(t).css('visibility', 'visible');
				}
			},
			changeSort : function(th) { // change sortorder
				if (this.loading){
					return true;
				}
				$(g.nDiv).hide();
				$(g.nBtn).hide();
				if (p.sortname == $(th).attr('abbr')) {
					if (p.sortorder == 'asc')
						p.sortorder = 'desc';
					else
						p.sortorder = 'asc';
				}

				$(th).addClass('sorted').siblings().removeClass('sorted');
				$('.sdesc', this.hDiv).removeClass('sdesc');
				$('.sasc', this.hDiv).removeClass('sasc');
				$('div', th).addClass('s' + p.sortorder);
				p.sortname = $(th).attr('abbr');

				if (p.onChangeSort){
					p.onChangeSort(p.sortname, p.sortorder);
				}else{
					this.populate();
				}
			},
			reloadData : function(){
				p.newp=1;
				this.populate();
			},
			populate : function() {
				if (!p.dataUrl){
					return false;
				}
				if (!p.newp){
					p.newp = 1;
				}
				p.page = p.newp;
				if (p.page > p.pages){
					p.page = p.pages;
				}
				
				var gridService;
				if (p.onSubmit) {
					if(!p.isAjax){//非ajax提交
						if(p.onSubmit(p.form)==false){
							return;
						}
					}else if(p.form){//存在form
						if(p.onSubmit(p.form.options().service)==false){
							p.form.service=null;
							return;
						}
					}else{//不存在form
						gridService=new W11Service(p.dataUrl);
						if(p.onSubmit(gridService)==false){
							return;
						}
					}
				}
				g.setParams({"pageSize":parseInt(p.pageSize),"page":parseInt(p.page)});
				
				if(!p.isAjax){
					p.form.action=p.dataUrl;
					p.form.submit();
					return true;
				}
				
				if (this.loading){
					return true;
				}
				this.loading = true;
				$(g.block).css({
				    top : g.bDiv.attr("offsetTop")
			    });
				if (p.hideOnSubmit){
					this.gDiv.prepend(g.block);
				}
				if ($.browser.opera){
					$(t).css('visibility', 'hidden');
				}
				
				//存在form
				if(p.form){
					p.form.options().action=p.dataUrl;
					var griddata=p.form.griddata;
					p.form.griddata=[{name:p.name,property:p.property}];
					p.form.ajaxsubmit();//查询
					p.form.griddata=griddata;//恢复原有数据
				}else{
					g.submitData();
					gridService=gridService==null?new W11Service(p.dataUrl):gridService;
					gridService.data=$(":input").serializeArray();
					gridService.callback=function(){
						fillGrid(gridService,p.name,p.property);
					}
					gridService.run();
				}
				//清空页数信息
				g.setParams("page",1);
			},
			// rebuild pager based on new properties
			buildpager : function() {
				var count=p.total=parseInt(p.total);
				var pageSize=p.pageSize=parseInt(p.pageSize);
				var currentPage=p.page=parseInt(p.page);
				
				if (!p.showPage) {
					return;
				}
				
				var multiPage='',multiPage1="",multiPage2="";
				var page=6;
				var prev = "上一页";
				var next = "下一页";
				if(count > pageSize) {
					var offset = 2;
					var pages =  Math.ceil(count / pageSize);
					var from,to;
					
					if(page > pages) {
						from = 1;
						to = pages;
					} else {
						from = currentPage - offset;
						to = from + page - 1;
						
						if(from < 1) {
							to = currentPage - from + 1;
							from = 1;
							if(to - from < page) {
								to = page;
							}
						} else if(to > pages) {
							from = pages - page + 1;
							to = pages;
						}
					}
					
					multiPage+=( (currentPage > 1 ? "<div class='page pPrev'><span>"+prev+"</span></div><div class='sep'></div>" : "") );
					multiPage+=( (currentPage - offset > 1 && pages > page ? "<div class='page first'><span>1 ...</span></div>" : "") );
					
					for(var i = from; i <= to; i++){
						multiPage+=( i == currentPage ? "<div class='page hover curr'><span>"+i+"</span></div>" :"<div class='page'><span class='pButton'>"+i+"</span></div>" );
					}
					multiPage+=( (to < pages ? "<div class='page last'><span>... "+pages+"</span></div>" : "") );
					multiPage+=(currentPage < pages ? "<div class='sep'></div><div class='page pNext'><span>"+next+"</span></div>" : "");
					if(multiPage.length>0){//跳转框
						multiPage+="<div class='sep'></div>";
						multiPage+="<div class='goto'><input type='text' size='20' /></div>";
						multiPage+="<div class='goto_button'><span>Go</span></div>";
					}
				}
				//套一个外包装
				multiPage1="<div class='pageicon'></div><div class='text'><span>共<em>"+count+"&nbsp;</em>条记录,每页显示</span></div>";
				//动态设置显示条数
				if (p.showPageSizeOptions) {
					var opt = '',sel = '',curOption;
					for (var nx = 0; nx < p.pageSizeOptions.length; nx++) {
						curOption=p.pageSizeOptions[nx];
						if (p.pageSize == curOption){
							sel = 'selected="selected"';
						}else{
							sel = '';
						}
						opt += "<option value='" + curOption + "' " + sel + " >" + curOption + "&nbsp;&nbsp;</option>";
					}
					multiPage2="<div class='pGroup'><select>" + opt + "</select></div>";
				}
				multiPage=multiPage1+multiPage2+(multiPage.length>0?"<div class='sep'></div>":'')+multiPage;
				$('.pages', g.pDiv).html(multiPage);
				
				if (p.showPageSizeOptions) {
					$('select', g.pDiv).change(function(){
						p.newp = 1;
						if (p.onChangePage){
							var result=p.onChangePage(p.newp);
							if (!!!result){
								return false;
							}
						}
						p.pageSize = this.value;
						g.populate();
					});
				}
				//验证数字
				if($(".goto input",g.pDiv).val() != undefined){
					$(".goto input",g.pDiv).blur(function(){
						var re = /^[1-9]\d*$/;
						if($(this).val()!=""&&!re.test($(this).val())){
							top.Dialog.alert("请输入格式正确的页面值！");
							$(this).val("");
							return false;
						}
						return true;
					})
				}
				multiPage=null;
				if ($.browser.msie && $.browser.version < 7.0) {//针对IE6设置鼠标移入移出样式
					$('.page', g.pDiv).not($('.hover',g.pDiv)).mouseover(function(){
						$(this).addClass('hover');
					}).mouseout(function(){
						$(this).removeClass('hover');
					})
				}
				$(".page").each(function() {
					if($(this).hasClass("pPrev")){
						$(this).click(function(){
							g.changePage("prev");
						});
					}else if($(this).hasClass("pNext")){
						$(this).click(function(){
							g.changePage("next");
						});
					}else if($(this).hasClass("first")){
						$(this).click(function(){
							g.changePage("first");
						});
					}else if($(this).hasClass("last")){
						$(this).click(function(){
							g.changePage("last");
						});
					}else if($(this).text()=="Go"){
						$(this).click(function(){
							g.changePage($(".goto").children().val());
						});
						
					}else{
						$(this).click(function(){
							g.changePage($(this).text());
						});
					}
				});
				$(".goto_button", g.pDiv).click(function(){
					g.changePage($(".goto",g.pDiv).children().val());
				});
			},
			changePage : function(ctype) { // change page
				if (this.loading){
					return true;
				}
				p.pages =  Math.ceil(p.total / p.pageSize);
				
				switch (ctype) {
					case 'first' :
						p.newp = 1;
						break;
					case 'prev' :
						if (p.page > 1)
							p.newp = parseInt(p.page) - 1;
						break;
					case 'next' :
						if (p.page < p.pages)
							p.newp = parseInt(p.page) + 1;
						break;
					case 'last' :
						p.newp = p.pages;
						break;
					default:
						if(ctype < 1){
							p.newp = 1;
						}else if(ctype > p.pages){
							p.newp = p.pages;
						}else{
							p.newp = ctype;
						}
						break;
				}
				if (p.newp == p.page){
					return false;
				}
				if (p.onChangePage){
					var result=p.onChangePage(p.newp);
					if (!!!result){
						return false;
					}
				}
				this.populate();
			},
			addCellProp : function(scope) {
				if(!!!scope){
					scope=$(t).children("tbody").children("tr");
				}
				
				var json=[],td,tdDiv,data;
				var pth,pdiv,ptr;
				scope.each(function() {
					$(this).children("td").each(function(i){
						td=$(this);
						data=json[i];
						if(data==null){
							data={};
							pth=p.model.th[i].th;
							pdiv=p.model.th[i].div;
							data.textAlign=pdiv.css("textAlign");
							data.width=pdiv.css("width");
							data.hide=pth.css("display")=="none";
							
							data.sorted=false;
							if (p.sortname && p.sortname==pth.attr('abbr')){
								data.sorted=true;
							}
							
							ptr=td.parent("tr");
							data.pid=false;
							if(ptr.id){
								data.pid = ptr.id;
							}
							data.process=false;
							if(pth.process){
								data.process=pth.process;
							}
							json[i]=data;
						}
						
						tdDiv=$("<div/>").css({"text-align":data.textAlign,"width":data.width}).html(td.html());
						if(data.hide){
							td.hide();
						}
						if(data.sorted){
							td.addClass("sorted");
						}
						if(data.process){
							data.process(tdDiv, data.pid);
						}
						// wrap content
						td.empty().append(tdDiv).removeAttr('width'); 
						// add editable event here 'dblclick'
					});
				});
				json=null;
			},
			addRowProp : function(scope) {
				if(!!!scope){
					scope=$(t).children("tbody").children("tr");
				}
				
				scope.each(function() {
					var tr=$(this);
					var hasTree=false;
					tr.children("td").each(function(i){
						var code=p.model.code[i];
						var type=code.type;//本列的类型
						var template=code.template.clone();//获取组件模板并复制一个对象
						var target=div=$(this).children("div");
						if(code.property==""&&code.replace!="true"){
							target=code.value;
						}
						var value=target.text();
						if(type=="text"){//初始化文本框
							div.addClass('inputDiv');
							var newInputText=template;
							var newTextSpan=$('<span/>').text(value);//创建一个文本层
							newInputText.val(value).hide();
							newTextSpan.click(function(){
								newTextSpan.hide();
								newInputText.parent().css('width',newInputText.parent().width()+3).css({'padding-left':0,'padding-right':5})
									.end().show().select();
							});
							newInputText.blur(function(){
								newInputText.parent().css('width',newInputText.parent().width()-3).css({'padding-left':4,'padding-right':4})
									.end().hide();
								newTextSpan.text(newInputText.val()).show();
							});
							if(code.code.attr("allowWrap")=="true"){
								newTextSpan.css('white-space', 'normal');
							}else{
								newTextSpan.addClass("noWrap");
							}
							div.empty().append(newTextSpan).append(newInputText);
						}else if(type=="label"){//初始化label显示用
							value=target.html();
							div.addClass('inputDiv');
							var newLabel=template;
							newLabel.val(value);
							var newTextSpan=$('<span/>').html(value);//创建一个文本层
							if(code.code.attr("allowWrap")=="true"){
								newTextSpan.css('white-space', 'normal');
							}else{
								newTextSpan.addClass("noWrap");
							}
							if(value!=""){
								newTextSpan.css("height","auto");
							}
							div.empty().append(newTextSpan).append(newLabel);
						}else if(type=="checkbox" || type=="radio"){//初始化checkbox或者radio
							div.addClass('checkboxDiv');
							var newCheckBox=template;
							newCheckBox.val(value);
							div.empty().append(newCheckBox);
							if(code.property!=""){
								var defaultValue=code.value.html();
								if(defaultValue!=""&&defaultValue==value){
									newCheckBox.attr("checked","true");
								}
							}
						}else if(type=="hidden"){//初始化隐藏域
							var newHiddenText=template;
							newHiddenText.val(value);
							div.empty().append(newHiddenText);
							div.parent().hide();
						}else if(type=='select'){//初始化下拉列表,如果表格中的值在select中不存在，留空
							div.addClass('selectDiv');//本列的值
							var newSelect=template.hide();//复制一个对象
							var newTextSpan=$('<span/>');//创建一个文本层
							var opt=newSelect.children("option[value='"+value+"']");
							if(opt.length!=0){//如果表格中的值，select中也有
								newSelect.val(value);
								newTextSpan.text(opt.text());
							}else{
								newTextSpan.text(newSelect.children("option:eq(0)").text());
							}
							
							if(code.code.attr("isOnlyShow")!="true"){
								newTextSpan.click(function(e){
									newTextSpan.hide();
									newSelect.parent().css('width',newSelect.parent().width()+3).css({'padding-left':0,'padding-right':5})
										.end().show().focus();
									$(g.bDiv).noSelect();
								});
								newSelect.blur(function(){
									newSelect.parent().css('width',newSelect.parent().width()-3).css({'padding-left':4,'padding-right':4})
										.end().hide();
									newTextSpan.text(newSelect.children('option:selected').text()).show();
									$(g.bDiv).noSelect(false);
								});
							}
							if(code.code.attr("allowWrap")=="true"){
								newTextSpan.css('white-space', 'normal');
							}else{
								newTextSpan.addClass("noWrap");
							}
							div.empty().append(newTextSpan).append(newSelect);
						}else if(type=="date"){//初始化日期
							div.addClass('inputDiv');
							var newInputText=template.first();
							var newTextSpan=$('<span/>').text(value);//创建一个文本层
							newInputText.val(value).hide().removeClass("inp_date");
							newTextSpan.click(function(){
								newTextSpan.hide();
								template.last().show()
								newInputText.parent().css('width',newInputText.parent().width()+3).css({'padding-left':0,'padding-right':5})
									.end().addClass("inp_date").show().datepicker();
							});
							if(code.code.attr("allowWrap")=="true"){
								newTextSpan.css('white-space', 'normal');
							}else{
								newTextSpan.addClass("noWrap");
							}
							div.empty().append(newTextSpan).append(newInputText).append(template.last().hide());
						}else if(type=="tree"){//初始化树
							hasTree=true;
							div.addClass('treeDiv');
							var newTree=template;
							var tree=$("#"+template.attr("id"));
							if(tree.length>0){
								var treeTd=tree.parent().parent();
								var rowspan=treeTd.attr("rowspan");
								rowspan=rowspan==null?1:parseInt(rowspan)+1;
								treeTd.attr("rowspan",rowspan);
								$(this).remove();
							}else{
								div.empty().append(newTree);
								var init=code.code.attr("init");
								if(init){eval(init);}
								//去除mouseover效果
								$(this).addClass("treetable").mouseover(function(){
									if(!tr.hasClass("trOver")){
										tr.children().addClass("treetable");
									}
								}).mouseout(function(){
									tr.children(":gt(0)").removeClass("treetable");
								}).click(function(event){
									event.stopPropagation()
								});
							}
						}else if(type=="textarea"){//初始化textarea
							div.addClass('inputDiv');
							var newTextarea=template;
							var newTextSpan=$('<span/>').text(value);//创建一个文本层
							newTextarea.val(value).hide();
							newTextSpan.click(function(){
								newTextSpan.hide();
								newTextarea.parent().css('width',newTextarea.parent().width()+3).css({'padding-left':0,'padding-right':5})
									.end().show().select();
							});
							newTextarea.blur(function(){
								newTextarea.parent().css('width',newTextarea.parent().width()-3).css({'padding-left':4,'padding-right':4})
									.end().hide();
								newTextSpan.text(newTextarea.val()).show();
							});
							if(code.code.attr("allowWrap")=="true"){
								newTextSpan.css('white-space', 'normal');
							}else{
								newTextSpan.addClass("noWrap");
							}
							div.empty().append(newTextSpan).append(newTextarea);
						}else if(type=="password"){//初始化密码框
							div.addClass('inputDiv');
							var newPassword=template;
							var newTextSpan=$('<span/>').text("********");//创建一个文本层
							newPassword.val(value).hide();
							newTextSpan.click(function(){
								newTextSpan.hide();
								newPassword.parent().css('width',newPassword.parent().width()+3).css({'padding-left':0,'padding-right':5})
									.end().show().select();
							});
							newPassword.blur(function(){
								newPassword.parent().css('width',newPassword.parent().width()-3).css({'padding-left':4,'padding-right':4})
									.end().hide();
								newTextSpan.text("********").show();
							});
							if(code.code.attr("allowWrap")=="true"){
								newTextSpan.css('white-space', 'normal');
							}else{
								newTextSpan.addClass("noWrap");
							}
							div.empty().append(newTextSpan).append(newPassword);
						}
					});
					
					//避免双击触发两次单击
					var TimeFn = null;
					//设置正文中每一行的点击等事件
					tr.click(function(event) {
						if(!hasTree){
							tr.siblings().removeClass("trOver trCur");
							tr.addClass("trOver trCur");
							if(p.allowClickSelect&&!event.ctrlKey&&!event.shiftKey){
								tr.removeClass('trSelected');
							}
						}
						tr.find('input[type=checkbox][linecheck=true],input[type=radio][linecheck=true]')
							.each(function(){
							var curChkRadio=$(this);
							//代码触发click时srcElement不存在
							if(!!event.srcElement){
								var type=event.srcElement.type;
								if(!!!type||(type!="checkbox"&&type!="radio")){
									this.checked=true;
								}
							}
						});
						if(p.onTrClick){
							clearTimeout(TimeFn);// 取消上次延时未执行的方法
							if(p.onTrDblclick){
								TimeFn = setTimeout(function(){//单击事件执行的代码    
									p.onTrClick(tr);
								},200);
							}else{
								p.onTrClick(tr);
							}
						}
					}).mousedown(function(event) {
						if(p.allowClickSelect){
							if(p.isSingleSelect){
								if(event.ctrlKey||event.shiftKey){
									$(this).siblings().removeClass('trSelected');
									$(this).toggleClass("trSelected");
									$(g.gDiv).noSelect();
								}
							}else{
								if(event.ctrlKey){
									tr.toggleClass("trSelected");
									$(g.gDiv).noSelect();
								}else if(event.shiftKey){
									var index1=tr.prevAll(".trSelected").first().index();
									if(index1==-1){
										index1=0;
									}
									var index2=tr.index();
									for(var i=index1;i<=index2;i++){
										tr.parent().children("tr:eq("+i+")").addClass("trSelected");
									}
									$(g.gDiv).noSelect();
								}
							}
						}
					}).mouseup(function() {
						if (p.allowClickSelect) {
							$(g.gDiv).noSelect(false);
						}
					});
					if(p.onTrDblclick){
						tr.dblclick(function(){
							clearTimeout(TimeFn);// 取消上次延时未执行的方法    
							p.onTrDblclick(tr);//双击事件的执行代码
						});
					}
				    if ($.browser.msie && $.browser.version < 7.0) {
				    	tr.hover(function() {
				    		tr.addClass('trOver');
				        }, function() {
				        	if(!tr.hasClass("trCur")){
				        		tr.removeClass('trOver');
				        	}
				        });
				    }
				});
			},
			getCellDim : function(obj) {// get cell prop for editable event
				var ht = parseInt($(obj).height());
				var pht = parseInt($(obj).parent().height());
				var wt = parseInt(obj.style.width);
				var pwt = parseInt($(obj).parent().width());
				var top = obj.offsetParent.attr("offsetTop");
				var left = obj.offsetParent.offsetLeft;
				var pdl = parseInt($(obj).css('paddingLeft'));
				var pdt = parseInt($(obj).css('paddingTop'));
				return {
					ht : ht,
					wt : wt,
					top : top,
					left : left,
					pdl : pdl,
					pdt : pdt,
					pht : pht,
					pwt : pwt
				};
			},
			getName : function(name,types){
				if(name==undefined){
					name=0;
				}
				if(!isNaN(name)){//序号
					name=parseInt(name);
					if(types){
						if(typeof(types)=="string"){
							var temp=types;
							types=[];
							types.push(temp);
						}
						var len=p.model.code.length;
						var type,count=0;//取匹配types任一个的第name个
						for(var i=0;i<len;i++){
							type=p.model.code[i].type;
							if(jQuery.inArray(type, types)!=-1){
								if(name==count){
									name=p.model.code[i].name;
									break;
								}
								count++;
							}
						}
					}else{
						name=p.model.code[name].name;
					}
				}else{
					name=p.name+"."+name;
				}
				return name;
			},
			resetLineNum : function(){
				if(p.showLineNum){
					$(t).children("tbody").children("tr").each(function(i){
						$(this).children("td:eq(0)").find("div").text(i+1);
					});
				}
			},
			getCellObject : function(row,col){
				var name=this.getName(col);
				var target=null;
				if(name!=""){
					target=$(t).children("tbody").children("tr").eq(row).find("[name='"+name+"']");
				}else{
					if(!isNaN(col)){
						col=parseInt(col);
						target=$(t).children("tbody").children("tr").eq(row).children("td").eq(col).children("div");
					}
				}
				return target;
			},
			getCellValue : function(row, col){
				if(col==null){
					col=row;
					row=this.getCheckedLine(null,"0");
					if(row==null){
						row=this.getCurrentLineNo();
					}
				}
				if(typeof(row)=="number"){
					var target=this.getCellObject(row, col);
					if(target!=null){
						if(target.filter("input,select,textarea").length>0){
							return target.val();
						}
						return target.text();
					}
				}
				return "";
			},
			setCellValue : function(row,col,value){
				var target=this.getCellObject(row, col);
				if(target!=null){
					var targetObj=target.filter("input");
					if(targetObj.length!=0){
						var type=targetObj.attr("type");
						if(type=="text"||type=="hidden"){
							targetObj.val(value).prev().text(value);
						}else if(type=="checkbox"||type=="radio"){
							targetObj.val(value)
						}else if(type=="password"){
							targetObj.val(value).prev().text("********");
						}
						return;
					}
					targetObj=target.filter("select");
					if(targetObj.length!=0){
						targetObj.val(value).prev().text(targetObj.children('option:selected').text());
						return;
					}
					targetObj=target.filter("textarea");
					if(targetObj.length!=0){
						targetObj.val(value).prev().text(value);
						return;
					}
					target.text(value);
				}
			},
			setCellHtml : function(row,col,html){
				if(typeof(col)=="string"){
					col=p.name+"."+col;
					var len=p.model.code.length;
					for(var i=0;i<len;i++){
						if(p.model.code[i].name==col){
							col=i;
						}
					}
				}
				$(t).children("tbody").children("tr").eq(row).children("td").eq(col)
					.children("div").html(html);
			},
			focusCell : function(row,col){
				var target=this.getCellObject(row, col);
				$(t).children("tbody").children("tr").eq(row).click();
				if(target!=null){
					var targetObj=target.filter("input");
					if(targetObj.length!=0){
						var type=targetObj.attr("type");
						if(type=="text"||type=="password"){
							targetObj.show().select().prev().hide();
						}else if(type=="checkbox"||type=="radio"){
							targetObj.focus();
						}
						return;
					}
					targetObj=target.filter("select");
					if(targetObj.length!=0){
						if(newSelect.attr("isOnlyShow")!="true"){
							targetObj.show().focus().prev().hide();
						}
						return;
					}
					targetObj=target.filter("textarea");
					if(targetObj.length!=0){
						targetObj.show().select().prev().hide();
						return;
					}
				}
			},
			delRow : function(){
				var curRow=$(t).children("tbody").children("tr.trOver");
				var beforeRow=curRow.prev();
				var len=curRow.length;
				if(len>0){
					p.total=p.total-len;
					curRow.remove();
					this.resetLineNum();
					beforeRow.click();
				}
			},
			delRowByIndex : function(index){
				var type=typeof(index);
				//合数组
				if(type=="number"){
					var temp=index;
					index=[];
					index.push(temp);
				}else if(type=="string"){
					index=index.split(",");
				}else if(type=="object"){//数组
				}
				//转数字
				var len=index.length;
				for(var i=0;i<len;i++){
					index[i]=parseInt(index[i]);
				}
				//去重复
				index=jQuery.unique(index);
				//排序
				index=index.sort();
				//删除
				len=index.length;
				if(len>0){
					for(var i=len-1;i>=0;i--){
						$(t).children("tbody").children("tr").eq(i).remove();
					}
					p.total=p.total-len;
					this.resetLineNum();
				}
			},
			delCheckedRow : function(col){
				var name=this.getName(col,["checkbox","radio"]);
				var target=null;
				if(name!=""){
					target=$(t).children("tbody").find("[name='"+name+"']");
				}
				if(target!=null){
					var checkedRows=target.filter(":checked").parent().parent().parent();
					var len=checkedRows.length;
					if(len>0){
						p.total=p.total-len;
						checkedRows.remove();
						this.resetLineNum();
					}
				}
			},
			delSelectedRow : function(){
				var selectedRows=$(t).children("tbody").children("tr.trSelected");
				var len=selectedRows.length;
				if(len>0){
					p.total=p.total-len;
					selectedRows.remove();
					this.resetLineNum();
				}
			},
			clear : function(){
				p.total=0;
				$("tbody", t).empty();
			},
			getCurrentLineNo : function(){
				var index=$(t).children("tbody").children("tr.trOver").index();
				index=index==-1?null:index;
				return index;
			},
			getCheckedLine : function(col,type){
				var name=this.getName(col,["checkbox","radio"]);
				var result=null,target=null;
				if(name!=""){
					target=$(t).children("tbody").find("[name='"+name+"']");
					result=[];
					target.filter(":checked").each(function(i){
						if(type=="0"){
							result.push($(this).parent().parent().parent().index());
						}else if(type=="1"){
							result.push($(this).val());
						}
					});
					var len=p.model.code.length;
					for(var i=0;i<len;i++){
						if(p.model.code[i].name==name){
							if(p.model.code[i].type=="radio"){
								if(result.length!=0){
									result=result[0];
								}else{
									result=null;
								}
							}
						}
					}
				}
				return result;
			},
			getSelectedLineNo : function(){
				var result=[];
				var selectedRows=$(t).children("tbody").children("tr.trSelected");
				selectedRows.each(function(){
					result.push($(this).index());
				}); 
				return result;
			},
			getRowCount : function(){
				return $(t).children("tbody").children("tr").length;
			},
			doCheckAllLine : function(col,checked){
				var name=this.getName(col);
				if(name!=""){
					$(t).children("tbody").find("[name='"+name+"'][type='checkbox']").each(function(i){
						if(checked==true||checked==false){
							this.checked=checked;
						}else{
							this.checked=!this.checked;
						}
					});
				}
			},
			enableButton: function(col){
				name=col==undefined?0:col;
				var btn;
				if(isNaN(name)){
					btn=$('div',g.tDiv).find("[name='"+name+"']");
				}else{
					btn=$('div',g.tDiv).find(".fbutton,.fbuttonDisable").eq(parseInt(name));
				}
				if(btn&&btn.length>0){
					btn.removeClass("fbuttonDisable").addClass("fbutton");
					btn=btn.get(0);
					if(btn.onclick2){
						btn.onclick=btn.onclick2;
						btn.onclick2=null;
					}
				}
			},
			disableButton: function(col){
				name=col==undefined?0:col;
				var btn;
				if(isNaN(name)){
					btn=$('div',g.tDiv).find("[name='"+name+"']");
				}else{
					btn=$('div',g.tDiv).find(".fbutton,.fbuttonDisable").eq(parseInt(name));
				}
				if(btn&&btn.length>0){
					btn.removeClass("fbutton").addClass("fbuttonDisable");
					btn=btn.get(0);
					if(btn.onclick&&!btn.onclick2){
						btn.onclick2=btn.onclick;
						btn.onclick=null;
					}
				}
			},
			hide: function(){
				$(t).parents("table:eq(0)").hide();
			},
			show: function(){
				$(t).parents("table:eq(0)").show();
			},
			hideColumn : function(cid, visible) {
				if (visible == null) {
					visible = false;
				}
				if (!visible && $('input:checked', g.nDiv).length < p.minColToggle){
					return false;
				}
				if(typeof(cid)=="string"){
					cid=p.name+"."+cid;
					var len=p.model.code.length;
					for(var i=0;i<len;i++){
						if(cid==p.model.code[i].name){
							cid=i;
							break;
						}
					}
				}
				
				var ncol = p.model.th[cid];
				if(!ncol){
					return false;
				}
				ncol=ncol.th;
				var cb = $('input[value=' + cid + ']', g.nDiv)[0];
				if (visible) {
					ncol.show();
					cb.checked = true;
				} else {
					ncol.hide();
					cb.checked = false;
				}
				
				$("tbody tr", t).each(function() {
				    if (visible){
				    	$('td:eq(' + cid + ')', this).show();
				    }else{
				    	$('td:eq(' + cid + ')', this).hide();
				    }
			    });
				this.rePosDrag();
				return true;
			},
			exportGrid: function(expType){
				if(expType==null){
					expType="EXCEL";
				}
				g.setParams("expType",expType);
				var action=p.form.action;
				p.form.action=p.dataUrl;
				p.form.method="post";
				p.form.submit();
				p.form.action=action;
				g.setParams("expType","");
			},
			submitData: function(){//提交grid数据(1)submit(2)onsubmit
				if(p.allowSubmitData){
					var count=this.getRowCount();
					var result=[],colResult,len;
					var nameSplit="#n#",rowSplit="#r#",colSplit="#c#";
					$.each(p.model.code,function(i,json){
						if(json.name==""||json.name==undefined){//seq,tree
							return true;
						}
						colResult=[];
						len=0;
						$(t).children("tbody").find("[name='"+json.name+"']").each(function(j){
							if(json.type=="checkbox"||json.type=="radio"){
								colResult.push(this.checked?"1":"0");
							}else{
								colResult.push(this.value);
							}
							len=j;
						});
						len=count-(len+1);//补空
						for(var j=0;j<len;j++){
							colResult.push("");
						}
						colData=json.name+nameSplit;
						if(colResult.length>0){
							colData+=colResult.join(rowSplit)+rowSplit;
						}
						result.push(colData);
					});
					g.setParams({"pageSize":parseInt(p.pageSize),"total":parseInt(p.total)});
					$("#"+p.gridData).val(result.join(colSplit));
				}
			},
			setParams: function(key,value){
				var type=(typeof key);
				var gridParams=$("#"+p.gridParams);
				var params=jQuery.parseJSON(gridParams.val()==""?"{}":gridParams.val());
				if(type=="string"){
					params[key]=value;
				}else if(type=="object"){
					$.extend(true,params,key);
				}
				gridParams.val($.toJSON(params));
			},
			resize: function(){
				var bdiv=$(g.bDiv);
				if(bdiv.filter(":visible").length==0){//兼容IE7,8
					return;
				}
				bdiv.height("10").children("table").hide();
				var adaptParent=$(t).parentsUntil("body")
						.filter("div.cardContent,table,form,td,tr").height("auto");
				
				var curAdaptParent,curHight;
				for(var i=adaptParent.length-1;i>=0;i--){//自上而下
					curAdaptParent=adaptParent.eq(i);
					curHight=curAdaptParent.parent().height()-
						(curAdaptParent.offset().top-curAdaptParent.parent().offset().top);
					curAdaptParent.height(curHight);
				}
				
				var bdivParent=bdiv.parent().parent();
				var h=bdivParent.innerHeight()-(bdiv.offset().top-bdivParent.offset().top);
				if(p.showPage){h=h-28;}//减去翻页高度
				bdiv.height(h).children("table").show();
				adaptParent.css("height","auto");
			},
			checkRow: function(index,checked){
				if(index==null){
					index=0;
				}
				var type=typeof(index);
				var tr=null;
				if(type=="number"){
					tr=$(t).children("tbody").children("tr:eq("+index+")");
				}else if(type=="string"){
					tr=$("#"+index, g.bDiv);
				}
				if(tr){
					tr.siblings().removeClass("trOver trCur");
					tr.addClass("trOver trCur");
					if(checked){
						tr.find("input[type=radio],input[type=checkbox]").first().attr("checked",true);
					}
				}
				return tr;
			},
			getRows: function(index){
				if(index==null){
					return $("tr", t);
				}
				var type=typeof(index);
				if(type=="number"){
					return $(t).children("tbody").children("tr").eq(index);
				}else if(type=="string"){
					index=index.split(",");
					if(index.length==1){
						return $("#"+index, g.bDiv);
					}
				}else if(type=="object"){//数组
				}
				var rowAry=[];
				var len=index.length,curIndex;
				for(var i=0;i<len;i++){
					curIndex=index[i];
					if(typeof(curIndex)=="number"){
						rowAry.push($(t).children("tbody").children("tr").eq(curIndex));
					}else if(typeof(curIndex)=="string"){
						rowAry.push($("#"+curIndex, t));
					}
				}
				return rowAry;
			},
			hideRows: function(index,hide){
				if(hide==null){
					hide=true;
				}
				var rows=this.getRows(index);
				var type=typeof(index);
				if(type=="object"){
					var len=rows.length;
					for(var i=0;i<len;i++){
						if(hide){rows[i].hide();}
						else{rows[i].show();}
					}
				}else{
					if(hide){rows.hide();}
						else{rows.show();}
				}
				return rows;
			},
			setLabel: function(label){
				$(g.mDiv).find(".ftitle").html(label);
				g.setParams({"expModel":{"exportFileName":label}});
			},
			setHeight: function(h){
				if(!isNaN(h)&&h>=0){
					$(g.bDiv).height(h);
					this.rePosDrag();
					this.fixHeight();
				}
			},
			setProperties: function(key,value){
				var type=(typeof key);
				if(type=="string"){
					p[key]=value;
				}else if(type=="object"){
					$.extend(true,p,key);
				}
			}
		};
		// build module
		var b = {
			$t : $(t),							//table
			buildModule : function(module){		//将模块添加到页面
				var divs=["nBtn","nDiv","mDiv","tDiv","hDiv","cDrag","bDiv","pDiv"];
				var className=module.get(0).className;
				if(g.gDiv.children("div."+className).length==0){
					var pos=jQuery.inArray(className, divs);
					if(pos!=-1){
						var target=null;
						for(var i=pos-1;i>=0;i--){
							target=g.gDiv.children("div."+divs[i]);
							if(target.length>0){
								target.after(module);
								return;
							}
						}
						for(var i=pos+1;i<=divs.length;i++){
							target=g.gDiv.children("div."+divs[i]);
							if(target.length>0){
								target.before(module);
								return;
							}
						}
						g.gDiv.prepend(module);
					}
				}
			},
			rebuild : function(callback){
				if(!p.modelUrl)return;
				
				var service=new W11Service(p.modelUrl);
				service.showLoading=false;
				service.callback=function(){
					var params=service.getBean(p.structureModel);
					b.buildButtons(params.buttons==""?null:$.evalJSON("{"+params.buttons+"}").buttons);
					b.buildHead($(params.thead));
					b.buildModel($(params.caption));
					b.buildDrag();
					b.buildColumnControl();
					g.rePosDrag();
					g.fixHeight();
					g.resize();
					
					if(callback){
						callback(service);
					}
				}
				service.run();
			}
		}
		// init divs
		g.gDiv = $('<div/>'); // create global container
		g.mDiv = $('<div/>'); // create title container
		g.hDiv = $('<div/>'); // create header container
		g.bDiv = $('<div/>'); // create body container
		g.vDiv = $('<div/>'); // create grip
		g.rDiv = $('<div/>'); // create horizontal resizer
		g.cDrag = $('<div/>'); // create column drag
		g.block = $('<div/>'); // creat blocker
		g.nDiv = $('<div/>'); // create column show/hide popup
		g.nBtn = $('<div/>'); // create column show/hide button
		g.iDiv = $('<div/>'); // create editable layer
		g.tDiv = $('<div/>'); // create toolbar
		g.sDiv = $('<div/>');
		g.pDiv = $('<div/>'); // create pager container
		g.hTable = $('<table/>');//create head table
		g.t = $(t);

		// set gDiv
		g.gDiv.addClass("flexigrid");
		// add conditional classes
		if ($.browser.msie){
			g.gDiv.addClass('ie');
		}
		if (p.width != 'auto'){
			g.gDiv.width(p.width);
		}
		if (p.novstripe){
			g.gDiv.addClass('novstripe');
		}
		g.t.before(g.gDiv);
		g.gDiv.append(t);

		// set button
		b.buildButtons=function(buttons){
			if(!buttons){
				g.tDiv.remove();
				return;
			}
			
			var tDiv2 = $('<div/>').addClass("tDiv2");
			var btnlen=buttons.length,btn;
			for (var i = 0; i < btnlen; i++) {
				btn = buttons[i];
				if (btn.separator) {
					tDiv2.append("<div class='btnseparator'></div>");
				}else{
					var btnDiv = $('<div/>').html("<div><span>" + btn.value + "</span></div>").noSelect();
					if (btn.name){
						btnDiv.attr("name", btn.name);
					}
					if (btn.icon){
						$('span', btnDiv).addClass(btn.icon).css({
						    paddingLeft : 20
					    });
					}
					if(btn.disabled){
						btnDiv.addClass("fbuttonDisable");
						btnDiv.get(0).onclick2=btn.onclick;
					}else{
						btnDiv.addClass("fbutton");
						btnDiv.get(0).onclick=btn.onclick;
					}
					tDiv2.append(btnDiv);
				}
			}
			if ($.browser.msie && $.browser.version < 7.0) {
				tDiv2.find(".fbutton,.fbuttonDisable").each(function(){
					var tBtn=$(this);
					tBtn.hover(function() {
						if(!tBtn.hasClass("fbuttonDisable")){
							tBtn.addClass('fbOver');
						}
				    }, function() {
				    	tBtn.removeClass('fbOver');
				    });
				});
			}
			g.tDiv.addClass("tDiv").empty()
				.append(tDiv2).append("<div style='clear:both'></div>");
			b.buildModule(g.tDiv);
		}
		b.buildButtons(p.buttons);

		// set hDiv
		b.buildHead=function(thead){
			if(!thead){
				g.hDiv.remove();
				return;
			}
			
			g.hDiv.empty().addClass('hDiv');
			b.buildModule(g.hDiv);
			
			g.hDiv.append('<div class="hDivBox"></div>')
				.one("mouseover",function(){
					g.rePosDrag();
					g.fixHeight();
				});
			// set hTable
			g.hTable.empty().attr({"cellPadding":0,"cellSpacing":0}).append(thead);
			g.hDiv.children("div").append(g.hTable);

			// setup thead
			g.hDiv.find("thead th").each(function(i) {
				var th=$(this).attr('axis', 'col' + i);
				var thdiv=$('<div/>').css("width", th.attr("width")).html(th.html());
				if (th.attr('abbr')) {
					th.click(function(e) {
					    if (!th.hasClass('thOver')){
					    	return false;
					    }
					    var obj = (e.target || e.srcElement);
					    if (obj.href || obj.type){
					    	return true;
					    }
					    g.changeSort(th);
				    });
					if (th.attr('abbr') == p.sortname) {
						th.removeClass().addClass("sorted");
						thdiv.removeClass().addClass('s' + p.sortorder);
					}
				}
				
				th.empty().append(thdiv).removeAttr('width')
					.mousedown(function(e) {
						if(p.allowMoveHead){
							g.dragStart('colMove', e, this);
						}
				    }).hover(function(){
						if (!g.colresize && !th.hasClass('thMove') && !g.colCopy){
							th.addClass('thOver');
						}
		
						if (th.attr('abbr') != p.sortname && !g.colCopy && !g.colresize && th.attr('abbr')){
							$('div', th).addClass('s' + p.sortorder);
						} else if (th.attr('abbr') == p.sortname && !g.colCopy && !g.colresize && th.attr('abbr')) {
							var no = 'asc';
							if (p.sortorder == 'asc'){
								no = 'desc';
							}
							$('div', th).removeClass('s' + p.sortorder).addClass('s' + no);
						}
		
						if (g.colCopy) {
							var n = $('th', g.hDiv).index(th);
							if (n == g.dcoln){
								return false;
							}
							if (n < g.dcoln){
								th.append(g.cdropleft);
							}else{
								th.append(g.cdropright);
							}
							g.dcolt = n;
						} else if (!g.colresize) {
							var nv = $('th:visible', g.hDiv).index(this);
							var onl = parseInt($('div:eq(' + nv + ')', g.cDrag).css('left'));
							var nw = parseInt(g.nBtn.width())
							    + parseInt(g.nBtn.css('borderLeftWidth'));
							nl = onl - nw + Math.floor(p.cgwidth / 2);
		
							g.nDiv.hide();
							g.nBtn.hide();
							g.nBtn.css({'left' : nl,top : g.hDiv.attr("offsetTop")}).show();
		
							var ndw = parseInt(g.nDiv.width());
		
							g.nDiv.css({top : g.bDiv.attr("offsetTop")});
		
							if ((nl + ndw) > g.gDiv.width()){
								g.nDiv.css('left', onl - ndw + 1);
							}else{
								g.nDiv.css('left', nl);
							}
							if (th.hasClass('sorted')){
								g.nBtn.addClass('srtd');
							}else{
								g.nBtn.removeClass('srtd');
							}
						}
					}, function() {
						th.removeClass('thOver');
						if (th.attr('abbr') != p.sortname){
							$('div', th).removeClass('s' + p.sortorder);
						}else if (th.attr('abbr') == p.sortname) {
							var no = 'asc';
							if (p.sortorder == 'asc'){
								no = 'desc';
							}
							$('div', th).addClass('s' + p.sortorder).removeClass('s' + no);
						}
						if (g.colCopy) {
							$(g.cdropleft).remove();
							$(g.cdropright).remove();
							g.dcolt = null;
						}
					}); // wrap content
			});
		}
		b.buildHead($("thead", t));

		// set bDiv
		g.bDiv.addClass('bDiv');
		g.t.before(g.bDiv);
		g.bDiv.scroll(function(e) {
			    g.scroll()
		   	}).append(t);
		if (p.height == 'auto') {
			g.t.addClass('autoht');
		}
		//set model
		b.buildModel=function(caption){
			p.model.th=[],p.model.code=[];
			
			var parents=b.$t.parentsUntil("body").filter("form:first");
			if(parents.length>0){
				p.form=parents.get(0);
			}
			
			g.hDiv.find("thead th").each(function(i) {
				var th=$(this),thdiv=th.children("div");
				p.model.th[i]={"th":th,"div":thdiv,"label":thdiv.text()};
			});
			b.$t.children("caption").remove().end().prepend(caption);
			
			var expModel={"showLineNum":p.showLineNum,"exportFileName":p.exportFileName,"fields":[]};
			caption.children("code").each(function(i){
				var code=$(this),value=code.children(".defaultValue").remove();
				var template=code.children();
				var type=code.attr("type");
				if(type=="hidden"){
					p.model.th[i].th.hide();
				}else if(type=="tree"){
					code.children().remove();
				}
				//全局模型
				p.model.code[i]={"code":code,"template":template,"type":type,"seq":i,
						"name":template.attr("name")==null?"":template.attr("name"),
						"property":template.attr("property")==null?"":template.attr("property"),
						"label":p.model.th[i].label,"value":value,
						"replace":code.attr("replace")==null?"false":code.attr("replace")};
				//导出模型
				code=p.model.code[i];
				expModel.fields[i]={"seq":code.seq,"label":code.label,"type":code.type,
						"name":code.name,"property":code.property,"content":"",
						"value":code.value.text(),"replace":code.replace};
				if(type=="select"){
					var content="";
					template.children("option").each(function(){
						content+=this.value+":"+this.text+","
					})
					content=content.length>0?content.substring(0,content.length-1):content;
					expModel.fields[i].content=content;
				}
			});
			
			//获取grid所属form,目的是为了表单提交时向后台提交grid数据
			var tc=$(document.body);
			if(p.form){
				var form=$(p.form),tc=form;
			    //(1)onsubmit
				form.unbind("submit",g.submitData).submit(g.submitData);
				//(2)submit w11.js 1030行
				var griddata=p.form.griddata;
				if(griddata==undefined){
					p.form.griddata=[];
					griddata=[];
				}
				var griddatalen=griddata.length;
				var gridObj,exist=false;
				for(var i=0;i<griddatalen;i++){
					gridObj=griddata[i];
					if(gridObj.name==p.name){
						exist=true;
						break;
					}
				}
				if(exist==false){
					p.form.griddata.push({name:p.name,property:p.property});
				}
			}
			
			//hidden grid data/params
			var ary=[p.gridData,p.gridParams];
			for(var i=0;i<ary.length;i++){
				var hi=$("#"+ary[i]).val("");
				if(hi.length==0){
					tc.append("<input type='hidden' name='"+ary[i]+"' id='"+ary[i]+"'/>");
				}
			}
			g.setParams({"name":p.name,"expModel":expModel,"showPage":p.showPage});
		}
		b.buildModel($('caption',g.bDiv));

		// set cDrag
		b.buildDrag=function(){
			g.cdpad = 0;
			if (p.model.th.length==0) {
				g.cDrag.remove();
				return;
			}
			
			var cdcol = p.model.th[0].th;
			var thDiv=$('div', cdcol);
			
			g.cdpad += (isNaN(parseInt(thDiv.css('borderLeftWidth')))
			    ? 0 : parseInt(thDiv.css('borderLeftWidth')));
			g.cdpad += (isNaN(parseInt(thDiv.css('borderRightWidth')))
			    ? 0 : parseInt(thDiv.css('borderRightWidth')));
			g.cdpad += (isNaN(parseInt(thDiv.css('paddingLeft')))
			    ? 0 : parseInt(thDiv.css('paddingLeft')));
			g.cdpad += (isNaN(parseInt(thDiv.css('paddingRight')))
			    ? 0 : parseInt(thDiv.css('paddingRight')));
			g.cdpad += (isNaN(parseInt(cdcol.css('borderLeftWidth')))
			    ? 0 : parseInt(cdcol.css('borderLeftWidth')));
			g.cdpad += (isNaN(parseInt(cdcol.css('borderRightWidth')))
			    ? 0 : parseInt(cdcol.css('borderRightWidth')));
			g.cdpad += (isNaN(parseInt(cdcol.css('paddingLeft')))
			    ? 0 : parseInt(cdcol.css('paddingLeft')));
			g.cdpad += (isNaN(parseInt(cdcol.css('paddingRight')))
			    ? 0 : parseInt(cdcol.css('paddingRight')));
			thDiv=null,cdcol=null;
			
			g.cDrag.empty().addClass('cDrag');
			b.buildModule(g.cDrag);
			
			var cdheight = g.bDiv.height();
			var hdheight = g.hDiv.height();

			g.cDrag.css({top : -hdheight + 'px'});
			var thLen=p.model.th.length;
			for(var i=0;i<thLen;i++){
				var cgDiv = $('<div/>');
			    g.cDrag.append(cgDiv);
			    if (!p.cgwidth){
			    	p.cgwidth = cgDiv.width();
			    }
			    cgDiv.css({
				        height : cdheight + hdheight
			        }).mousedown(function(e) {
				        g.dragStart('colresize', e, this);
			        });
			}
			
			if ($.browser.msie && $.browser.version < 7.0) {
			    g.fixHeight(g.gDiv.height());
			    g.cDrag.children().each(function(){
			    	var cgDiv=$(this);
			    	cgDiv.hover(function() {
				        g.fixHeight();
				        cgDiv.addClass('dragging')
			        }, function() {
				        if (!g.colresize){
				        	cgDiv.removeClass('dragging')
				        }
			        });
			    });
		    }
		}
		b.buildDrag();
		
		if (p.allowResize && p.height != 'auto') {
			g.vDiv.addClass('vGrip');
			g.bDiv.after(g.vDiv);
			g.vDiv.mousedown(function(e) {
				    g.dragStart('vresize', e)
			    }).html('<span></span>');
		}

		if (p.allowResize && p.width != 'auto' && !p.nohresize) {
			g.rDiv.addClass('hGrip');
			g.rDiv.mousedown(function(e) {
				    g.dragStart('vresize', e, true);
			    }).html('<span></span>').css('height', g.gDiv.height());
			if ($.browser.msie && $.browser.version < 7.0) {
				g.rDiv.hover(function() {
					g.rDiv.addClass('hgOver');
				}, function() {
				    g.rDiv.removeClass('hgOver');
				});
			}
			g.gDiv.append(g.rDiv);
		}
		
		// add pager
		if (p.showPage) {
			g.pDiv.addClass('pDiv').html('<div class="pDiv2"></div>');
			g.bDiv.after(g.pDiv);
			
			$('div', g.pDiv).html("<div class='pages'></div>");
			//生成翻页
			g.buildpager();
		}
		$(g.pDiv, g.sDiv).append("<div style='clear:both'></div>");

		// add label
		b.buildLabel=function(label){
			if (label===false||label===null){
				g.mDiv.remove();
				return;
			}
			g.mDiv.empty().addClass('mDiv').html('<div class="ftitle">' + label + '</div>');
			b.buildModule(g.mDiv);
			
			if (p.allowMinisize) {
				g.mDiv.append('<div class="ptogtitle" title="最大化/最小化"><span></span></div>');
				$('div.ptogtitle', g.mDiv).click(function() {
				    g.gDiv.toggleClass('hideBody');
				    $(this).toggleClass('vsble');
			    });
			}
		}
		b.buildLabel(p.label);

		// setup cdrops
		g.cdropleft = document.createElement('span');
		g.cdropleft.className = 'cdropleft';
		g.cdropright = document.createElement('span');
		g.cdropright.className = 'cdropright';

		// add block
		g.block.addClass('gBlock');
		var gh = g.bDiv.height();
		var gtop = g.bDiv.attr("offsetTop");
		$(g.block).css({
			    width : g.bDiv.css("width"),
			    height : gh,
			    background : 'white',
			    position : 'relative',
			    marginBottom : (gh * -1),
			    zIndex : 1,
			    top : gtop,
			    left : '0px'
		    });
		$(g.block).fadeTo(0, p.blockOpacity);
		
		//add column control
		b.buildColumnControl=function(){
			if (!p.allowHideColumn||p.model.th.length==0) {
				g.nDiv.remove();
				return;
			}
			g.nBtn.empty().addClass('nBtn').html('<div></div>').attr('title',
			    '隐藏/显示列').click(function() {
				    g.nDiv.toggle();
				    return true;
			    });
			b.buildModule(g.nBtn);
			
			g.nDiv.empty().addClass('nDiv').html("<table cellpadding='0' cellspacing='0'><tbody></tbody></table>");
			g.nDiv.css({
				    marginBottom : (g.bDiv.height() * -1),
				    display : 'none',
				    top : g.bDiv.attr("offsetTop")
			    }).noSelect();

			$.each(p.model.code,function(i,json){
				var display='',type=json.type,chk='checked="checked"';
				if(type=="hidden"||type=="seq"){
					display="none",chk='';
				}
				$('tbody', g.nDiv)
				    .append('<tr style="display:'+display+'">'
				    			+'<td class="ndcol1"><input type="checkbox" '+chk+' class="togCol" value="' + i+ '" /></td>'
				    			+'<td class="ndcol2">' + json.label+ '</td>'
				    		+'</tr>');
			});

			if ($.browser.msie && $.browser.version < 7.0){
				$('tr', g.nDiv).hover(function() {
				    $(this).addClass('ndcolover');
			    }, function() {
				    $(this).removeClass('ndcolover');
			    });
			}
			$('td.ndcol2', g.nDiv).click(function() {
				var ck=$(this).prev().find('input')[0];
				if ($('input:checked', g.nDiv).length <= p.minColToggle
						&& ck.checked == true){
					ck.checked=true;
					return false;
				}
				return g.hideColumn(parseInt(ck.value),!ck.checked);
			});
			$('input.togCol', g.nDiv).click(function() {
				if ($('input:checked', g.nDiv).length < p.minColToggle
						&& this.checked == false){
					this.checked=true;
					return false;
				}
				g.hideColumn(parseInt(this.value),this.checked);
			});
			b.buildModule(g.nDiv);
		}
		b.buildColumnControl();

		// add date edit layer
		g.iDiv.addClass('iDiv').hide();
		g.bDiv.append(g.iDiv);

		// add flexigrid events
		g.bDiv.hover(function() {
			g.nDiv.hide();
			g.nBtn.hide();
		});
		g.gDiv.hover(function() {
		    }, function() {
			    g.nDiv.hide();
			    g.nBtn.hide();
		    });

		// add document events
		$(document).mousemove(function(e) {
			    g.dragMove(e)
		    }).mouseup(function(e) {
			    g.dragEnd()
		    }).hover(function() {
		    }, function() {
			    g.dragEnd()
		    });

		// browser adjustments
		if ($.browser.msie && $.browser.version < 7.0) {
			$('.hDiv,.bDiv,.mDiv,.pDiv,.vGrip,.tDiv, .sDiv', g.gDiv).css({
				    width : '100%'
			    });
			g.gDiv.addClass('ie6');
			if (p.width != 'auto'){
				g.gDiv.addClass('ie6fullwidthbug');
			}
		}
		
		//高度
		if(p.height=="adapt"){//自适应
			g.resize();
			onWindowResize.add(g.resize);
			g.bDiv.attr("adapt","true");
		}else if(p.height!="auto"){
			g.bDiv.height(p.height);
		}
		
		g.rePosDrag();
		g.fixHeight();
		// make grid functions accessible
		t.p = p;
		t.grid = g;
		t.b = b;
		// load data
		if(p.modelUrl&&p.structureModel){
			var callback=null;
			if(p.dataUrl && p.autoLoad){
				callback=g.populate;
			}
			b.rebuild(callback);
		} else if (p.dataUrl && p.autoLoad) {
			g.populate();
		} else {
			// add td properties
			g.addCellProp();
			// add row properties
			g.addRowProp();
		}
		// onInit
		if (p.onInit) {
			p.onInit();
		}
		return t;
	};
	
	var docloaded = false;
	$(document).ready(function() {
	    docloaded = true
    });

	var gridPlugin = jQuery.sub();
	jQuery.fn.grid = function() {
		return gridPlugin(this);
	}
	/**
	 * 初始化grid
	 */
	gridPlugin.fn.initialize = function(options) {
		return this.each(function() {
			var t = this;
			if(!options){
				options=eval(t.id+"_options");
			}
		    if (!docloaded) {
			    $(this).hide();
			    $(document).ready(function() {
			        $.addGrid(t, options);
		        });
		    } else {
			    $.addGrid(t, options);
		    }
	    });
	};
	/**
	 * 增加一行
	 */
	gridPlugin.fn.addRow = function(cell,id) {
		return this.each(function() {
			this.grid.addRow(cell,id);
		});
	};
	/**
	 * 插入一行
	 */
	gridPlugin.fn.insertRow = function(index,cell,id) {
		return this.each(function() {
			this.grid.insertRow(index,cell,id);
		});
	};
	/**
	 * 填充表格数据,多用于翻页
	 */
	gridPlugin.fn.addData = function(service,data) {
		return this.each(function() {
	    	this.grid.addData(service,data);
	    });
	};
	/**
	 * 刷新当前页数据
	 */
	gridPlugin.fn.refresh = function() {
		return this.each(function() {
	    	this.grid.populate();
	    });
	};
	/**
	 * 取得第row行，第col列的域对象
	 */
	gridPlugin.fn.getCellObject = function(row,col) {
		var result;
		this.each(function() {
			result=this.grid.getCellObject(row,col);
		});
		return result;
	};
	/**
	 * 取得第row行，第col列的域的值
	 */
	gridPlugin.fn.getCellValue = function(row,col) {
		var result;
		this.each(function() {
			result=this.grid.getCellValue(row,col);
		});
		return result;
	};
	/**
	 * 为第row行，第col列的域赋值value
	 */
	gridPlugin.fn.setCellValue = function(row,col,value) {
		return this.each(function() {
			this.grid.setCellValue(row,col,value);
		});
	};
	/**
	 * 为第row行，第col列的域赋html， 覆盖里面原有的域
	 */
	gridPlugin.fn.setCellHtml = function(row,col,html) {
		return this.each(function() {
			this.grid.setCellHtml(row,col,html);
		});
	};
	/**
	 * 光标聚焦到第row行，第col列的域
	 */
	gridPlugin.fn.focusCell = function(row,col) {
		return this.each(function() {
			this.grid.focusCell(row,col);
		});
	};
	/**
	 * 删除当前选中行
	 * class=trOver
	 */
	gridPlugin.fn.delRow = function() {
		return this.each(function() {
			this.grid.delRow();
		});
	};
	/**
	 * 根据行号删除指定行
	 */
	gridPlugin.fn.delRowByIndex = function(index){
		return this.each(function() {
			this.grid.delRowByIndex(index);
		});
	};
	/**
	 * 删除checkbox,radio选中的行
	 * 参数checkcol为name值或列的索引值
	 * 不传参时取这一行第一个checkbox,radio的索引值
	 */
	gridPlugin.fn.delCheckedRow = function(col){
		return this.each(function() {
			this.grid.delCheckedRow(col);
		});
	};
	/**
	 * 删除选中行
	 */
	gridPlugin.fn.delSelectedRow = function() {
		return this.each(function() {
			this.grid.delSelectedRow();
		});
	};
	/**
	 * 选中指定行
	 */
	gridPlugin.fn.checkRow = function(index,checked) {
		var result;
		this.each(function() {
			result=this.grid.checkRow(index,checked);
		});
		return result;
	};
	/**
	 * 获取指定行
	 */
	gridPlugin.fn.getRows = function(index) {
		var result;
		this.each(function() {
			result=this.grid.getRows(index);
		});
		return result;
	};
	/**
	 * 隐藏指定行
	 */
	gridPlugin.fn.hideRows = function(index) {
		var result;
		this.each(function() {
			result=this.grid.hideRows(index);
		});
		return result;
	};
	/**
	 * 显示指定行
	 */
	gridPlugin.fn.showRows = function(index) {
		var result;
		this.each(function() {
			result=this.grid.hideRows(index,false);
		});
		return result;
	};
	/**
	 * 清空grid
	 */
	gridPlugin.fn.clear = function(){
		return this.each(function() {
			this.grid.clear();
		});
	};
	/**
	 * 获取当前行的行号
	 */
	gridPlugin.fn.getCurrentLineNo = function() {
		var result;
		this.each(function() {
			result=this.grid.getCurrentLineNo();
		});
		return result;
	};
	/**
	 * 获取checkbox,radio选中行的行号
	 * 参数checkcol为name值或列的索引值
	 * 不传参时取这一行第一个checkbox,radio的索引值
	 */
	gridPlugin.fn.getCheckedLineNo = function(col){
		var result;
		this.each(function() {
			result=this.grid.getCheckedLine(col,"0");
		});
		return result;
	};
	/**
	 * 获取checkbox,radio选中行的值
	 * 参数checkcol为name值或列的索引值
	 * 不传参时取这一行第一个checkbox,radio的索引值
	 */
	gridPlugin.fn.getCheckedLineValue = function(col){
		var result;
		this.each(function() {
			result=this.grid.getCheckedLine(col,"1");
		});
		return result;
	};
	/**
	 * 获取选中行的行号
	 */
	gridPlugin.fn.getSelectedLineNo = function(){
		var result;
		this.each(function() {
			result=this.grid.getSelectedLineNo();
		});
		return result;
	};
	/**
	 * 返回表格体的总行数
	 */
	gridPlugin.fn.getRowCount = function(){
		var result;
		this.each(function() {
			result=this.grid.getRowCount();
		});
		return result;
	};
	/**
	 * 使所有行的checkbox选中或取消选中
	 * 参数checkcol为name值或列的索引值
	 * 不传参时取这一行第一个checkbox的索引值
	 * 参数为true时所有行checkbox都选中,为false时所有行checkbox都取消选中
	 * 不传时选中与不选中来回交替
	 */
	gridPlugin.fn.doCheckAllLine = function(col,checked) {
		return this.each(function() {
			this.grid.doCheckAllLine(col,checked);
		});
	};
	/**
	 * 启用button 
	 */
	gridPlugin.fn.enableButton = function(col) {
		return this.each(function() {
			this.grid.enableButton(col);
		});
	};
	/**
	 * 禁用button 
	 */
	gridPlugin.fn.disableButton = function(col) {
		return this.each(function() {
			this.grid.disableButton(col);
		});
	};
	/**
	 * 隐藏
	 */
	gridPlugin.fn.hide = function() {
		return this.each(function() {
			this.grid.hide();
		});
	};
	/**
	 * 显示
	 */
	gridPlugin.fn.show = function() {
		return this.each(function() {
			this.grid.show();
		});
	};
	/**
	 * 设置标题
	 */
	gridPlugin.fn.setLabel = function(label) {
		return this.each(function() {
			this.grid.setLabel(label);
		});
	};
	/**
	 * 隐藏/显示某列(列序号/true或false)
	 */
	gridPlugin.fn.hideColumn = function(cid, visible) {
    	return this.grid.hideColumn(cid, visible);
	};
	/**
	 * 高度自适应
	 */
	gridPlugin.fn.setHeight = function(h){
		return this.each(function() {
			this.grid.setHeight(h);
		});
	};
	/**
	 * 导出
	 */
	gridPlugin.fn.exportGrid = function(expType) {
		return this.each(function() {
			this.grid.exportGrid(expType);
		});
	};
	/**
	 * 内部函数，不对外使用
	 * 提交数据
	 */
	gridPlugin.fn.submitData = function(){
		return this.each(function() {
			this.grid.submitData();
		});
	};
	/**
	 * 高度自适应
	 */
	gridPlugin.fn.resize = function(){
		return this.each(function() {
			this.grid.resize();
		});
	};
	/**
	 * 设置grid属性
	 */
	gridPlugin.fn.setProperties = function(key,value){
		return this.each(function() {
			this.grid.setProperties(key,value);
		});
	};
	/**
	 * 重建
	 */
	gridPlugin.fn.rebuild = function(callback){
		return this.each(function() {
			this.b.rebuild(callback);
		});
	};
	/**
	 * 重新加载数据
	 */
	gridPlugin.fn.reloadData = function(){
		return this.each(function() {
			this.grid.reloadData();
		});
	};
})(jQuery);