# Minecraft server stress tool
## Usage
`-a,--ip <arg>            server adress`  
 `-d,--delay <arg>         delay in ms between bot joins (default 5000ms)`  
 `-l,--logincmd <arg>      login command withcout slash`  
 `-n,--nick <arg>          base string for nicknames`  
 `-p,--port <arg>          port number`  
 `-r,--registercmd <arg>   register command witchout slash`  
 `-t,--threadnum <arg>     threads (bots) number`
`
### Example usage:
`java -jar minecraft_stress_tool-all-1.0-SNAPSHOT.jar" --ip 127.0.0.1 --port 25565 --delay 3000 --logincmd login --registercmd register -t 5 -n bot`
