# Owere
## 헤어디자이너들의 프리랜서활동, 비상주 미용공간 제공을 위한 플랫폼       
(쓱싹 -> 오월, 상표등록문제로 이름변경)
![image](https://user-images.githubusercontent.com/68258365/147375072-e7c62a50-babe-4af7-bbf2-f04b9c96c575.png)
<img src="https://user-images.githubusercontent.com/68258365/144905364-f284a8e4-b7e2-4291-b374-209eac363f77.png" width="200" height="400"/>
### Flow diagram
![image](https://user-images.githubusercontent.com/68258365/147375084-7e63fbb0-7437-4ae1-8b70-80e6e05c02c5.png)

![image](https://user-images.githubusercontent.com/68258365/147375086-b6247633-fd51-481c-a15e-419bec597998.png)


## 1.기술스택         
프레임워크 : 안드로이드 스튜디오(코틀린) 4.1.2버전        
서버와 DB : Firebase, Firebase Realtime Database, Storage        
라이브러리 : glide, jetpack, 카카오 맵 api, material-calendarview,firebase-auth       

## 2. 지원 기능         
### - 회원가입, 로그인         
###   구글계정으로 회원가입,로그인 가능         
###   유저,디자이너 선택하여 회원가입(디자이너 가입조건 추가예정)    
![image](https://user-images.githubusercontent.com/68258365/144905400-64f40bf6-57c0-427b-950b-523eb8799207.png)
![image](https://user-images.githubusercontent.com/68258365/144905416-d0365be0-c7af-41ab-b9d0-2ba896ab06a0.png)


### - 홈 화면(유저)         
####  지금 이용가능한 주변 (5km이내) 디자이너 목록         
####  즐겨찾기 디자이너         
![image](https://user-images.githubusercontent.com/68258365/144905538-a251aa25-fd4e-4997-a620-996775d9725a.png)


### - 디자이너 프로필 화면(유저)                  
####  디자이너가 설정한 프로필, 소개, 포트폴리오, 메뉴, 리뷰 조회          
####  디자이너와 채팅, 예약 기능         
![image](https://user-images.githubusercontent.com/68258365/144905560-8be8b5a6-aae8-475b-9b30-f6af3d4ff298.png)
![image](https://user-images.githubusercontent.com/68258365/144905593-df4f0503-c4e7-4696-ab55-fc6ba24c6530.png)


### - 디자이너 예약(유저)         
#### 원하는 메뉴 선택         
#### 지도에 나온 디자이너가 속한 미용실중 한곳 선택         
#### 선택한 미용실 이용 가능한 시간 표시, 시간 선택       
![image](https://user-images.githubusercontent.com/68258365/144905661-1379e898-e91e-4e74-9863-2e4acee850c2.png)
![image](https://user-images.githubusercontent.com/68258365/144905683-74f62250-49f1-4a07-9ecd-ad55f77f0104.png)

![image](https://user-images.githubusercontent.com/68258365/144905757-d2470ad0-7d00-4fc6-8bb7-fed376772d0c.png)

### - 예약 내역(유저)         
#### 확정된 예약 -> 예약 취소 기능         
#### 대기중 예약         
#### 완료된 예약         
![image](https://user-images.githubusercontent.com/68258365/144905790-cd6bc1b5-54c7-454d-adc7-9b822610fea5.png)


### - 채팅기능(유저, 디자이너)         
####  디자이너 - 유저, 미용실 업주 - 유저, 디자이너 - 업주 간 채팅 기능 
![image](https://user-images.githubusercontent.com/68258365/144905834-36db14e7-f54d-4716-9d21-17dba7af0ee2.png)



### - 마이페이지(유저)         
####  내 정보, 공지사항, 프로모션, 알림설정, 환경설정, 오월소개, 내 후기(서비스 준비중), 관심디자이너,         
### - 둘러보기 (유저) (서비스 준비중)         
![image](https://user-images.githubusercontent.com/68258365/144905884-1ea73956-4174-4392-a20a-2fc14b96e937.png)
![image](https://user-images.githubusercontent.com/68258365/144905897-bcb3a534-d434-4d8e-8b5c-9a8911e18e17.png)


### - 홈 화면(디자이너)         
####  예정된 예약 목록, 예정된 예약 미용실 위치 정보 표시(지도), 대기중 예약 목록   
![image](https://user-images.githubusercontent.com/68258365/147375120-fc482b21-421f-401b-ba4b-8400e87ef346.png)
![image](https://user-images.githubusercontent.com/68258365/147375122-24d0c7f9-4afd-4160-9c63-8ffc9268d67f.png)
![image](https://user-images.githubusercontent.com/68258365/147375123-c60137c5-27d4-489b-95fa-ae2885235dd8.png)
![image](https://user-images.githubusercontent.com/68258365/147375126-1701e159-d8ec-4262-8f90-36544b9c42ac.png)

####  예약 상세 정보 확인 기능, 예약 거절 기능   

![image](https://user-images.githubusercontent.com/68258365/144905956-66fa8bf8-16cc-4644-899b-a4d87a2ed50e.png)

### - 마이페이지(디자이너)         
![image](https://user-images.githubusercontent.com/68258365/147375037-dacfb688-f099-4f7c-801d-087971acd7ad.png)
![image](https://user-images.githubusercontent.com/68258365/147375038-d10b8f6e-385b-4d97-9143-3ac28e322fda.png)
![image](https://user-images.githubusercontent.com/68258365/147375040-337f1966-7b8b-477a-b548-f0b43f3cac0e.png)

####  공지사항, 프로모션, 알림설정, 환경설정, 오월소개, (서비스 준비중) -> 리뷰 관리,단골고객,쿠폰함, 등급         


### - 둘러보기 (디자이너) (서비스 준비중)         
### - 채용 (서비스 준비중)         
