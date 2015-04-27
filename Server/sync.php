<?php
include 'config.php';
$req=json_decode($_GET['data']);
$con=mysqli_connect($host,$user,$password,$dbname) or die(mysqli_error($con));
for ($i=0;$i<count($req);$i++)
{
	$course_id=$req[$i]->course_id;
	$semester=$req[$i]->semester;
	$year=$req[$i]->year;
	$date=$req[$i]->date;
	$id=$req[$i]->id;
	$attendance=$req[$i]->attendance;
	echo "INSERT INTO `attendance`(`course_id`, `semester`, `year`, `date`, `id`, `attendance`) VALUES ($course_id,$semester,$year,$date,$id,$attendance)";
	mysqli_query($con,"INSERT INTO `attendance`(`course_id`, `semester`, `year`, `date`, `id`, `attendance`) VALUES (\"$course_id\",$semester,$year,\"$date\",\"$id\",$attendance)") or die ("Error  = ".mysqli_error($con));
}
?>