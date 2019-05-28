DROP TABLE Clients CASCADE CONSTRAINTS;
/
DROP TABLE Games CASCADE CONSTRAINTS;
/
DROP TABLE Libraryes CASCADE CONSTRAINTS;
/
DROP TABLE Reviews CASCADE CONSTRAINTS;
/
DROP TABLE Game_sequels CASCADE CONSTRAINTS;
/
DROP TABLE Game_discount CASCADE CONSTRAINTS;
/
DROP TABLE Categories CASCADE CONSTRAINTS;
/
DROP TABLE History CASCADE CONSTRAINTS;
/
CREATE TABLE Clients (
	id INT NOT NULL PRIMARY KEY,
	username VARCHAR2(50) NOT NULL unique,
	password VARCHAR2(40) NOT NULL, 
	email VARCHAR2(50) NOT NULL unique,
	payment_method VARCHAR2(25),
	wallet FLOAT)
/
CREATE TABLE Games(
	id INT NOT NULL PRIMARY KEY,
	nume VARCHAR2(30) NOT NULL,
	pret FLOAT,
	oferta INT, /* 1 daca este la reducere, 0 contrar */
	release_date DATE,
	sequel_id INT)
/
CREATE TABLE Libraryes (
	id INT NOT NULL PRIMARY KEY,
	user_id INT NOT NULL,
	game_id INT NOT NULL,
	CONSTRAINT fk_Libraryes_user_id FOREIGN KEY (user_id) REFERENCES Clients(id),
	CONSTRAINT fk_Libraryes_game_id FOREIGN KEY (game_id) REFERENCES Games(id),
  CONSTRAINT unique_key UNIQUE (user_id,game_id))
/

CREATE TABLE Categories (
	id INT NOT NULL PRIMARY KEY,
	category VARCHAR2(50) NOT NULL,
	game_id INT NOT NULL,
	CONSTRAINT fk_Categories_game_id FOREIGN KEY (game_id) REFERENCES Games(id))
/

CREATE TABLE Reviews (
	id INT NOT NULL PRIMARY KEY,
	game_id INT NOT NULL,
	reviewer_id INT NOT NULL,
	stars INT NOT NULL,
	Message VARCHAR2(300),
	CONSTRAINT fk_Reviews_game_id  FOREIGN KEY (game_id) REFERENCES Games(id),
	CONSTRAINT fk_Reviews_reviewer_id FOREIGN KEY (reviewer_id) REFERENCES Clients(id))
/
	
CREATE TABLE Game_sequels (
	id INT NOT NULL PRIMARY KEY,
	nume VARCHAR2(30) NOT NULL)
/

CREATE TABLE Game_discount (
	id INT NOT NULL PRIMARY KEY,
	game_id INT NOT NULL,
	discount_percent INT NOT NULL,
	start_discount DATE,
	end_discount DATE,
	CONSTRAINT fk_dis_game_id FOREIGN KEY (game_id) REFERENCES Games(id))
/
CREATE TABLE history (
  user_id INT NOT NULL,
  game_id INT NOT NULL,
  action_date DATE,
  CONSTRAINT fk_user_id_history foreign key (user_id) references clients(id),
  CONSTRAINT fk_game_id_history foreign key (game_id) references games(id)
)
/

CREATE OR REPLACE TRIGGER HistoryChange
before update or INSERT
ON libraryes
FOR EACH ROW 

DECLARE
  v_id_game int;
  v_id_user int;
BEGIN 

  INSERT INTO history VALUES 
  (:new.user_id,:new.game_id, sysdate); 

END;
/

create index i_user
on clients (username);
/
create index i_mail
on clients (email);
/

DECLARE 
	TYPE sirStringuri IS VARRAY(5000) OF varchar2(255);
  nume sirStringuri := sirStringuri('Albu','Alexa','Anton','Andronic','Ardeleanu','Asachi','Avramescu','Baciu','Badea','Baicu',
			'Balanici','Barbu','Bejenaru','Begu','Bentoiu','Bercu','Besoiu','Botez','Botnariu','Bratu',
			'Burada','Bujor','Camataru','Captaru','Carafoli','Cazacu','Cebotari','Celan','Cernea','Chelaru',
			'Cojocaru','Comarnescu','Conea','Corbea','Cordos','Corduneanu','Cordeanu','Cosmescu','Craciunescu','Cristescu',
			'Damaschin','Danciu','Dejeu','Diaconu','Dinu','Dobroiu','Dumitrescu','Eliade','Enescu','Filimon',
			'Florea','Florescu','Ganea','Gliga','Grecu','Herlea','Iancu','Ivanescu');
	prenume sirStringuri :=sirStringuri('Marcel','Diana','George','Vasile','Ion','Adina','Cecilia','Mark','Veronica','Stefania',
			'Mihai','Bianca','Victoria','Maria','Beniamin','Petre','Teo','Artur','Luminita','Bianca','Ionela','Carla',
			'Cami','Ecaterina','Costel','Valeriu','Ovidiu','Adrian','Mirela','Gabriela','Daniel','Emima','Sandu','Flavius',
			'Elisabeta','Denisa','Tatiana','Horatiu','Oana','Ivan','Violeta','Victoria','Valerica','Magda','Delia');
	games 	sirStringuri :=sirStringuri('Minecraft','Doom','Assasins Creen Odyssey','Borderlands','Fortnite','Spore','PlayerUnknown BattleGrounds','Darksiders',
			'Grand Theft Auto','The Elder Scrolls','Fallout','The Witcher','Ark: Survival Evolved',
			'Call of Duty','Diablo','World of Warcraft','Batman','Darksiders','Cities:Skylines',
			'Sea of Thieves','Final Fantasy','Dead Island','Warframe','Star Wars','Overwatch','Forza Horizon',
			'Apex Legends','De Blob','Anthem','The Walking Dead','Metal Gear Solid','Rocket League','Gears of War','Tom Clancys The Division');
  categorii sirStringuri :=sirStringuri('Action','Platform games','Shooter games','Fighting games','Beat em up games','Stealth game','Survival games',
          'Rhythm games','Action-adventure',
          'Survival horror',
          'Adventure',
          'Visual novels',
          'Real-time 3D adventures',
      'Role-playing',
          'Action RPG',
          'MMORPGFantasy',
          'Simulation',
          'Construction and management simulation',
          'Life simulation',
          'Vehicle simulation',
          'Strategy',
          'Artillery game',
          'Real-time strategy (RTS)',
          'Real-time tactics (RTT)',
          'Multiplayer online battle arena (MOBA)',
          'Tower defense',
          'Wargame',
          'Racing',
          'Sports game',
          'Competitive',
          'Logic game');
	paymentMethod sirStringuri :=sirStringuri('Paypal','MasterCard','PaySafeCard');
  TYPE vector IS VARRAY(5000) OF INT;
  v_visiting vector := vector(0);
	--VARIABLES
	v_index INT;
	v_nume VARCHAR2(255);
	v_prenume VARCHAR2(255);
	v_username VARCHAR2(255);
	v_password VARCHAR2(255);
	v_paymethod VARCHAR2(255);
	v_wallet INT;
	v_price INT;
	v_date DATE;
	v_oferta INT;
	v_percent INT;
	v_index_gd INT :=0;
	v_fDate DATE;
	v_lDate DATE;
	v_sequel INT;
	v_sequel_index INT :=0;
	v_sequel_name VARCHAR2(255);
	v_sequel_n INT;
  v_index2 INT;
  v_dec INT :=0;
  v_games_count INT;
  v_users_count INT := 100000;
  v_library_games_count INT;
  v_library_count INT;
  v_gameid INT;
  v_category_count INT;
  v_id_cat INT;
  v_ti INT;
  V_mail VARCHAR2(50);
BEGIN
	FOR v_index IN 1..v_users_count LOOP
		v_nume :=nume (TRUNC(DBMS_RANDOM.VALUE(0,nume.COUNT))+1);
		v_prenume :=prenume(TRUNC(DBMS_RANDOM.VALUE(0,prenume.COUNT))+1);
		v_ti := (TRUNC(DBMS_RANDOM.VALUE(1,2)));
    IF (v_ti = 1)
    then v_username :=v_prenume||'_'||'1'||(TRUNC(DBMS_RANDOM.VALUE(1,500)))||v_nume||(TRUNC(DBMS_RANDOM.VALUE(1,500)))||(TRUNC(DBMS_RANDOM.VALUE(1,500)));
		else v_username :=v_nume||'_'||'2'||(TRUNC(DBMS_RANDOM.VALUE(1,500)))||v_prenume||(TRUNC(DBMS_RANDOM.VALUE(1,500)))||(TRUNC(DBMS_RANDOM.VALUE(1,500)));
    end if;
    v_password :=(TRUNC(DBMS_RANDOM.VALUE(5,100)))||(CHR(TRUNC(DBMS_RANDOM.VALUE(33,126))))||(TRUNC(DBMS_RANDOM.VALUE(5,100)))||(CHR(TRUNC(DBMS_RANDOM.VALUE(33,126))));
		v_paymethod :=paymentMethod(TRUNC(DBMS_RANDOM.VALUE(0,paymentMethod.COUNT))+1);
		v_wallet :=DBMS_RANDOM.VALUE(0,1000);
		if (v_ti = 2) 
    then
      V_mail := v_nume||(TRUNC(DBMS_RANDOM.VALUE(1,20)))||'_1'||(TRUNC(DBMS_RANDOM.VALUE(1,50)))||v_prenume||(TRUNC(DBMS_RANDOM.VALUE(1,2000)))||'@gmail.com';
      insert into Clients (id,username,password,email,payment_method,wallet)
      select v_index,v_username,v_password,V_mail,v_paymethod,v_wallet from dual 
      where not exists (select 1 from clients where username=v_username or email=V_mail);
		else
      V_mail := v_prenume||(TRUNC(DBMS_RANDOM.VALUE(1,20)))||'_2'||(TRUNC(DBMS_RANDOM.VALUE(1,50)))||v_nume||(TRUNC(DBMS_RANDOM.VALUE(1,2000)))||'@yahoo.com';
      insert into Clients 
      select v_index,v_username,v_password,V_mail,v_paymethod,v_wallet from dual
      where not exists (select 1 from clients where username=v_username or email=V_mail);
		end if;
    
    v_nume:='';
		v_prenume:='';
		v_username:='';
		v_password:='';
		v_paymethod:='';
		v_wallet:='';
	END LOOP;
  v_users_count := v_users_count +1;
  insert into Clients values (v_users_count,'beni','beni','beni@gmail.com','paypal',850);
  dbms_output.put_line('Insert in clients Done !');
  
	FOR v_index IN 1..games.COUNT LOOP
		v_nume:=Games(v_index);
		v_price:=DBMS_RANDOM.VALUE(0,60);
		v_oferta:=DBMS_RANDOM.VALUE(0,1);
		v_sequel:=DBMS_RANDOM.VALUE(0,1);
		v_date:=sysdate-DBMS_RANDOM.VALUE(0,100)*30-DBMS_RANDOM.VALUE(0,20)*365;
		IF(v_sequel > 0) THEN
			v_sequel_name:=v_nume;
      v_sequel_n:=DBMS_RANDOM.VALUE(2,5)-1;
			FOR v_index2 IN 0..v_sequel_n LOOP
				insert into Games values(v_index + v_dec + v_index2,v_nume||' '||(v_index2 + 1),v_price,v_oferta,v_date,(v_sequel_index+1));
				v_price:=DBMS_RANDOM.VALUE(0,60);
				v_oferta:=DBMS_RANDOM.VALUE(0,1);
				v_date:=sysdate-DBMS_RANDOM.VALUE(0,100)*30-DBMS_RANDOM.VALUE(0,20)*365;
			END LOOP;
        v_dec:=v_dec + v_sequel_n;
		ELSE 
			insert into Games values(v_index + v_dec,v_nume,v_price,v_oferta,v_date,null);
		END IF;
		IF(v_sequel > 0) THEN
			v_sequel_index:=v_sequel_index + 1;
			insert into Game_sequels values(v_sequel_index,v_sequel_name);
		END IF;
		IF (v_oferta > 0) THEN 
			v_index_gd := v_index_gd + 1;
			v_percent := DBMS_RANDOM.VALUE(5,95);
			v_fDate := sysdate - DBMS_RANDOM.VALUE(0,50);
			v_lDate := sysdate + DBMS_RANDOM.VALUE(7,30);
			insert into Game_discount values(v_index_gd,v_index+v_dec,v_percent,v_fDate,v_lDate);
		END IF;
	END LOOP;
  dbms_output.put_line('Insert in games,sequel,discount Done !');
  v_library_count :=0;
  select count(*) into v_games_count from games;
  v_visiting.extend(v_games_count);
  FOR v_index IN 1..v_users_count LOOP
    v_library_games_count := DBMS_RANDOM.VALUE(5,17);
    FOR v_index2 IN 1..v_games_count LOOP
      v_visiting(v_index2) := 0;
    END LOOP;
    FOR v_index2 IN 1..v_library_games_count LOOP
      v_gameid := DBMS_RANDOM.VALUE(1,v_games_count);
      IF(v_visiting(v_gameid) < 1) THEN 
          v_library_count := v_library_count + 1;
          insert into libraryes 
          select v_library_count,v_index,v_gameid from dual
          where exists (select 1 from clients where id=v_index);
          v_visiting(v_gameid) := 1;
      END IF;
    END LOOP;
  END LOOP;
  dbms_output.put_line('Insert in library Done !');
  v_id_cat:=0;
  FOR v_index IN 1..categorii.COUNT LOOP
    v_category_count := ceil(DBMS_RANDOM.VALUE(0,10));
    FOR v_index2 IN 1..v_category_count LOOP
      v_id_cat:=v_id_cat + 1;
      v_gameid :=DBMS_RANDOM.VALUE(1,v_games_count);
      v_nume := categorii(v_index);
      insert into categories values(v_id_cat,v_nume,v_gameid);
    END LOOP;
  END LOOP;
  
  dbms_output.put_line('Insert in category Done !');
  
  FOR v_index IN 1..10000 LOOP
    v_gameid := DBMS_RANDOM.VALUE(1,v_games_count);
    v_index2 := DBMS_RANDOM.VALUE(1,v_users_count);
    v_id_cat := DBMS_RANDOM.VALUE(1,5);
    insert into reviews 
    select v_index,v_gameid,v_index2,v_id_cat,null from dual 
    where exists(select 1 from clients where id=v_index2);
  END LOOP;
  dbms_output.put_line('Insert in review Done !');

END;
/

select 'clients',count(*) from clients union
select 'games',count(*) from games union
select 'game_sequels',count(*) from game_sequels union
select 'game_discount',count(*) from game_discount union
select 'libraryes',count(*) from libraryes union
select 'categories',count(*) from categories union
select 'reviews',count(*) from reviews union
select 'history',count(*) from history;
/
