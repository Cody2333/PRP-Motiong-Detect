##本次试验中raff文件生成步骤

命令行输入

	node transform n walk zou zou
	node transform a run pao zou
	node transform a statuc jing zou
	node transform a turnard zhuan zou
	node transform a tumble diedao zou

./raff/zou.raff 中15行左右

	@attribute class
中添加

	{walk,run,static,turnard,tumble}
成为

	@attribute class {zou,pao,jing,diedao,zhuan}

##transform 命令格式说明

	node transform [argv] [tablename] [inputfilename] [outputfilename]

####argv:
n： 新建arff文件

a：在原有文件上添加内容

#### tablename：
arff中添加的分类的名字

####inputfilename：
输入文件的名字，要求输入文件放入./raw文件夹中

####outputfilename：
输出文件的名字，文件会在./raff文件夹中输出