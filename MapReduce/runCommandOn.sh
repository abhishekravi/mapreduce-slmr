$(sed -n "$1p" run_commands.txt | sed "s/%/$2/g")
