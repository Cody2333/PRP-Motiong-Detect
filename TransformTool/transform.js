function turn(x, y, z, a, b, c) {
	var Pi = 3.14159;
	/*
	a 表示手机自身的y轴与地磁场北极方向的角度，即手机顶部朝向与正北方向的角度。
	b 表示手机顶部或尾部翘起的角度。
	c 表示手机左侧或右侧翘起的角度。
	x,y,z表示原坐标系即自然绝对坐标系中向量的坐标
	转换算法中，x,y,z为a,b,c为0时，即自然世界坐标。
	手机向北平放，g的坐标 0,0,-g
	坐标旋转顺序:
	z a，
	x b，
	y c

	-g 可通过空间旋转算法算出 在手机坐标中的坐标， 即可之间相减。
	*/
	//角度弧度转换
	a = a/180*Pi;
	b = b/180*Pi;
	c = c/180*Pi;

	// z a
	var t = x;
	x = x * Math.cos(a) - y * Math.sin(a);
	y = t * Math.sin(a) + y * Math.cos(a);

	//x b
	t = y;
	y = y * Math.cos(b) - z * Math.sin(b);
	z = t * Math.sin(b) + z * Math.cos(b);
	//y c
	t = x;
	x = x * Math.cos(c) + z * Math.sin(c);
	z = -t * Math.sin(c) + z * Math.cos(c);
	var option = new Array(x, y, z);

	return option;

}


//node transform [argv] [name] [append] [appendout]
//argv a/n
//name class name
var rf = require("fs");

var filein = './raw/';
var fileout = './arff/';
var append = process.argv[4];
var appendout = process.argv[5] || append;
var argv = process.argv[2]; //n for new ,a for append
var name = process.argv[3];

filein += append;
fileout += appendout;

rf.readFile(filein, 'utf-8', function(err, data) {
	if (err) {
		console.log(err);
		console.log("error");
	} else {
		var array = data.split('/r/n');

		//splar[][]二维数组
		//smv1 smv2....
		//x1   x2  ....
		//y1   y2  ....
		//...（z，time，A，P，R，time2，a，b，c，time3）
		var splar = [];
		for (var i = 0; i < array.length; i++) {
			splar[i] = new Array();
		}
		for (var i = 0; i < array.length; i++) {
			splar[i] = array[i].split(',');
			splar[i].pop();
		}

		//处理重力
		var g = 9.7940 //上海g
			//处理splar数组 除去重力影响
		for (var i = 0; i < splar[0].length; i++) {//每一大行
			for (var j = 0; j < splar.length; j++) {//每一组数据 splar[j][i]
				if(j===splar.length-1){
					var x = splar[j-11][i];
					var y = splar[j-10][i];
					var z = splar[j-9][i];
					var gra = [];
					gra = turn(0,0,-g,splar[j-7][i],splar[j-6][i],splar[j-5][i]);
					splar[j-11][i] = parseFloat(x)+gra[0];
					splar[j-10][i] = parseFloat(y)+gra[1];
					splar[j-9][i] = parseFloat(z)+gra[2];//原因不明，传来的z轴正方向沿手机正面向里，与文档不符
					splar[j-12][i] = Math.sqrt(splar[j-11][i]*splar[j-11][i]+splar[j-10][i]*splar[j-10][i]+splar[j-9][i]*splar[j-9][i]);
					var turnresult = turn(x,y,z,-splar[j-7][i],-splar[j-6][i],-splar[j-5][i]);
					console.log(turnresult);
					splar[j-8][i] = turnresult[2]-g;
					splar[j-4][i] = Math.sqrt(turnresult[0]*turnresult[0]+turnresult[1]*turnresult[1]);

					//将陀螺仪的时间戳改为矢量和
					splar[j][i]=Math.sqrt(splar[j-3][i]*splar[j-3][i]+splar[j-2][i]*splar[j-2][i]+splar[j-1][i]*splar[j-1][i]);
				}
			}
			arr += '\r\n';
		}


		if (argv == 'n') {
			var arr = '@relation ' + append + '\r\n';
			arr += '@attribute SMV real\r\n';
			arr += '@attribute X real\r\n';
			arr += '@attribute Y real\r\n';
			arr += '@attribute Z real\r\n';
			arr += '@attribute SMV-Z real\r\n';
			arr += '@attribute Azimuth real\r\n';
			arr += '@attribute Pitch real\r\n';
			arr += '@attribute Roll real\r\n';
			arr += '@attribute SMV-H real\r\n';
			arr += '@attribute gyr-a real\r\n';
			arr += '@attribute gyr-b real\r\n';
			arr += '@attribute gyr-c real\r\n';
			arr += '@attribute SMV-gyr real\r\n';
			arr += '@attribute class \r\n';
			arr += '@data\r\n';

			for (var i = 0; i < splar[0].length; i++) {
				for (var j = 0; j < splar.length; j++) {
					arr += splar[j][i];
					if (j != splar.length - 1)
						arr += ',';
					else {
						arr += ',' + name;
					}
				}
				arr += '\r\n';
			}
		} else if (argv == 'a') {
			var arr = '';
			for (var i = 0; i < splar[0].length; i++) {
				for (var j = 0; j < splar.length; j++) {
					arr += splar[j][i];
					if (j != splar.length - 1)
						arr += ',';
					else {
						arr += ',' + name;
					}
				}
				arr += '\r\n';
			}
		}
		rf.appendFile(fileout + '.arff', arr, function(err) {
			if (err)
				console.log("fail " + err);
			else
				console.log("写入文件ok");
		});
	}
});