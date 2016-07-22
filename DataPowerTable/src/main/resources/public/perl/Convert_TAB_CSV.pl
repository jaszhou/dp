use strict;
use warnings;
use POSIX qw/strftime/;

my $file = "D:\\download\\MongoDB\\blog\\src\\main\\resources\\public\\test\\DFAT.csv";
my $outfile = "D:\\download\\MongoDB\\blog\\src\\main\\resources\\public\\test\\DFAT_List.csv";
my $logfile = "D:\\download\\MongoDB\\blog\\src\\main\\resources\\public\\test\\convert_frd.log";



open (my $fh,$file) or die "could not open file $!";
open (my $fh_out, ">", $outfile) or die "could not open file $!";
open (my $fh_log, ">", $logfile) or die "could not open file $!";

my $line_num=0;
while (<$fh>) {
	$line_num++;
	
 	chomp($_);
    my @info = split("\t");
    
    my $arrSize = @info;
    
    if($info[0]=~/[a-z]/ && $line_num!=1){
    	next;
    }
    
    my $count=0;
    
    for ($count = 0; $count < $arrSize-1; $count++){
    	
#    	if($info[$count]=~/,/){
#    		print $fh_out "\"". $info[$count]."\",";
#    	}else{
#    		print $fh_out $info[$count].",";
#    	}
    		print $fh_out "\"". $info[$count]."\",";
    	
    }
	print $fh_out "\"". $info[$count]."\""."\n";
#    print $fh_out $info[$count]."\n";
    
    
}
 
 close ($fh);
 close ($fh_out);
 close ($fh_log);



