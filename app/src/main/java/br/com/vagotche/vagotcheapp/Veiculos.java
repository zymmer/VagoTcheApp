package br.com.vagotche.vagotcheapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by guilherme on 03/09/17.
 */

public class Veiculos {

    List<String> marcasModelos = Arrays.asList (
                "Fiat Bravo	",
                "Ford Ka	",
                "Honda Civic	",
                "Honda Fit	",
                "Peugeot 207	",
                "Fiat Uno	",
                "Honda City	",
                "Kia Cerato	",
                "Chery QQ	",
                "Citroen C3	",
                "Mini Cooper	",
                "Peugeot 307	",
                "Bmw X1	",
                "Ford Fiesta	",
                "Ford Focus	",
                "Hyundai i30	",
                "Jeep 	",
                "Audi A3	",
                "Bmw X6	",
                "Chery Face	",
                "Citroen Aircross	",
                "Fiat Punto	",
                "Ford Fusion	",
                "Hyundai ix35	",
                "Hyundai Sonata	",
                "Kia Picanto	",
                "Peugeot 3008	",
                "Renault Sandero	",
                "Ssangyong 	",
                "Audi A1	",
                "Audi R8	",
                "Chevrolet Camaro	",
                "Citroen C4	",
                "Fiat 500	",
                "Ford Edge	",
                "Mitsubishi Asx	",
                "Peugeot 408	",
                "Renault Fluence	",
                "Chevrolet Malibu	",
                "Dodge Journey	",
                "Fiat Stilo	",
                "Kia Sorento	",
                "Nissan Livina	",
                "Porsche Cayenne	",
                "Toyota Corolla	",
                "Aston Martin 	",
                "Audi A8	",
                "Audi TT	",
                "Chevrolet Agile	",
                "Fiat Idea	",
                "Fiat Strada	",
                "Nissan Frontier	",
                "Volvo C30	",
                "Volvo XC60	",
                "Audi A4	",
                "Bmw X5	",
                "Chery Tiggo	",
                "Fiat Linea	",
                "Lamborghini Gallardo	",
                "Nissan Tiida	",
                "Renault Logan	",
                "Subaru Impreza	",
                "Bmw Z4	",
                "Chrysler 300C	",
                "Citroen C5	",
                "Fiat Palio	",
                "Hyundai Tucson	",
                "Nissan Sentra	",
                "Peugeot 207 Passion	",
                "Audi Q7	",
                "Effa M100	",
                "Fiat Doblo	",
                "Ford Ranger	",
                "Iveco Daily	",
                "Porsche Panamera	",
                "Renault Symbol	",
                "Suzuki SX4	",
                "Audi A7	",
                "Ferrari California	",
                "Fiat Siena	",
                "Hyundai HR	",
                "Jeep Cherokee	",
                "Kia Cadenza	",
                "Honda Accord	",
                "Kia Bongo	",
                "Land Rover Defender	",
                "Nissan 350z	",
                "Nissan March	",
                "Peugeot Hoggar	",
                "Volkswagen Gol	",
                "Audi Q5	",
                "Bmw X3	",
                "Citroen C4 Pallas	",
                "Ferrari 458 Italia	",
                "Ford Ecosport	",
                "Hyundai Azera	",
                "Hyundai Santa Fe	",
                "Mitsubishi Outlander	",
                "Peugeot 407	",
                "Renault Master	",
                "Renault Scenic	",
                "Ssangyong Actyon	",
                "Subaru Forester	",
                "Suzuki Jimny	",
                "Volkswagen Fox	",
                "Audi A5	",
                "Audi A6	",
                "Bmw M5	",
                "Chevrolet Captiva	",
                "Ferrari F430	",
                "Fiat Ducato	",
                "Land Rover Freelander	",
                "Mitsubishi L200	",
                "Porsche 911	",
                "Smart Fortwo	",
                "Toyota Camry	",
                "Volvo S60	",
                "Audi S3	",
                "Chevrolet Celta	",
                "Chevrolet Classic	",
                "Chevrolet Montana	",
                "Chevrolet Prisma	",
                "Chevrolet Tracker	",
                "Citroen C4 Picasso	",
                "Ford Courier	",
                "Honda Civic Si	",
                "Kia Carens	",
                "Kia Mohave	",
                "Porsche Cayman	",
                "Ssangyong Kyron	",
                "Suzuki Grand Vitara	",
                "Toyota RAV4	",
                "Chevrolet Omega	",
                "Ford Fiesta Sedan	",
                "Peugeot Partner	",
                "Renault Kangoo	",
                "Volkswagen Amarok	",
                "Volvo XC70	",
                "Aston Martin DBS	",
                "Audi RS6	",
                "Chevrolet Blazer	",
                "Chevrolet S10	",
                "Ford New Fiesta	",
                "Honda CR0V	",
                "Land Rover Discovery	",
                "Mini Countryman	",
                "Peugeot 207 SW	",
                "Peugeot 307 Sedan	",
                "Subaru Impreza WRX	",
                "Subaru Tribeca	",
                "Troller T4	",
                "Volvo S40	",
                "Aston Martin DB9	",
                "Audi RS5	",
                "Chery Cielo Hatch	",
                "Chevrolet Vectra	",
                "Citroen Xsara Picasso	",
                "Effa Plutus	",
                "Fiat Palio Weekend	",
                "Ford Focus Sedan	",
                "Jaguar XF	",
                "Jeep Grand Cherokee	",
                "Kia Carnival	",
                "Peugeot Boxer	",
                "Porsche Boxster	",
                "Toyota Hilux SW4	",
                "Volkswagen Tiguan	",
                "Volvo XC90	",
                "Audi TTS	",
                "Chevrolet Zafira	",
                "Fiat Uno Mille	",
                "Ford F0250	",
                "Jac Motors J3	",
                "Mercedes C200	",
                "Mercedes Classe B	",
                "Nissan Grand Livina	",
                "Peugeot 307 CC	",
                "Renault Megane Grand Tour	",
                "Ssangyong Actyon Sports	",
                "Subaru Impreza WRX STI	",
                "Subaru Legacy	",
                "Volkswagen Golf	",
                "Volkswagen Voyage	",
                "Bentley Continental GT	",
                "Chevrolet Meriva	",
                "Chrysler PT Cruiser	",
                "Citroen C3 Picasso	",
                "Ferrari 599	",
                "Fiat Palio Adventure	",
                "Maserati Quattroporte	",
                "Mercedes Classe C	",
                "Mitsubishi L200 Triton	",
                "Porsche 911 Turbo	",
                "Subaru Outback	",
                "Volkswagen Eos	",
                "Volkswagen Jetta	",
                "Chana Cargo	",
                "Chevrolet Opala	",
                "Citroen Jumper	",
                "Ferrari 612 Scaglietti	",
                "Hyundai Veracruz	",
                "Mercedes Sprinter	",
                "Mitsubishi Pajero TR4	",
                "Volkswagen Bora	",
                "Volkswagen Touareg	",
                "Audi S6	",
                "Chery Cielo Sedan	",
                "Chevrolet Monza	",
                "Citroen C3 XTR	",
                "Ford F0350	",
                "Ford F04000	",
                "Mercedes CLK	",
                "Mitsubishi Lancer Evolution	",
                "Mitsubishi Pajero Sport	",
                "Ssangyong Rexton	",
                "Volkswagen Saveiro	",
                "Volvo C70	",
                "Volvo S80	",
                "Chana Family	",
                "Chevrolet Corsa Sedan	",
                "Ferrari F355	",
                "Fiat Fiorino Furgão	",
                "Kia K2500	",
                "Peugeot 407 SW	",
                "Volkswagen Kombi	",
                "Volkswagen Parati	",
                "Volvo V50	",
                "Audi A4 Avant	",
                "Chevrolet Chevette	",
                "Fiat Doblo Cargo	",
                "Jaguar X0Type	",
                "Lamborghini Gallardo Spyder	",
                "Land Rover Range Rover	",
                "Maserati Grancabrio	",
                "Mercedes Classe E	",
                "Mitsubishi Pajero Full	",
                "Volkswagen Passat	",
                "Audi RS6 Avant	",
                "Bmw Série 1	",
                "Chevrolet Corsa Hatch	",
                "Chevrolet Vectra GT	",
                "Chrysler Town & Country	",
                "Ferrari 612	",
                "Jac Motors J5	",
                "Maserati GranTurismo	",
                "Mitsubishi L200 Outdoor	",
                "Mitsubishi Pajero Dakar	",
                "Renault Grand Scenic	",
                "Toyota Hilux Cabine Simples	",
                "Volkswagen Crossfox	",
                "Chana Alsvin	",
                "Chana Benni	",
                "Chevrolet Astra Hatch	",
                "Chevrolet Kadett	",
                "Hyundai i30CW	",
                "Mercedes Classe S	",
                "Nissan XTRAIL	",
                "Porsche 911 Carrera	",
                "Renault Megane Sedan	",
                "Toyota Hilux Cabine Dupla	",
                "Toyota Land Cruiser Prado	",
                "Volkswagen Fusca	",
                "Volkswagen SpaceFox	",
                "Aston Martin V8 Vantage	",
                "Bentley Continental Supersports	",
                "Bmw Série 5	",
                "Jaguar XJ6	",
                "Mitsubishi L200 Savana	",
                "Ssangyong C200	",
                "Subaru Impreza Sedan	",
                "Toyota Corolla Fielder	",
                "Volkswagen New Beetle	",
                "Volkswagen Polo Sedan	",
                "Bmw Série 3	",
                "Bmw Série 7	",
                "Chana Benni Mini	",
                "Chrysler 300C Touring	",
                "Citroen C5 Tourer	",
                "Citroen Grand C4 Picasso	",
                "Jac Motors J6	",
                "Mercedes Classe G	",
                "Renault Kangoo Express	",
                "Mini Cabrio	",
                "Mini Cooper Clubman	",
                "Audi A6 Avant	",
                "Bentley Continental Flying Spur	",
                "Bentley Continental GTC	",
                "Chana Utility	",
                "Chevrolet Astra Sedan	",
                "Mercedes Classe M	",
                "Volkswagen Passat CC	",
                "Volkswagen Polo Hatch	",
                "Bmw M3 Sedan	",
                "Ferrari F360 Modena	",
                "Fiat Uno Furgão	",
                "Iveco Strallis	",
                "Mercedes Classe R	",
                "Mitsubishi L200 GL	",
                "Ford Courier Van	",
                "Jac Motors J3 Turin	",
                "Volkswagen Jetta Variant	",
                "Audi S6 Avant	",
                "Maserati GranSport	",
                "Chevrolet Maverick	",
                "Mercedes Classe CL	",
                "Mercedes Classe GL	",
                "Mitsubishi Air Track	",
                "Mercedes Classe GLK	",
                "Mitsubishi Pajero Full 3D	",
                "Mitsubishi Pajero HD	",
                "Bmw M1 Coupe	",
                "Kia Magnetis	",
                "Mercedes Classe C Touring	",
                "Mercedes Classe CLC	",
                "Mercedes Classe CLS	",
                "Bmw Série 1 Cabrio	",
                "Citroen Jumper Furgão	",
                "Citroen Jumper Mini Bus	",
                "Citroen Jumper Vetrato	",
                "Dodge Ram pickup	",
                "Effa Hafei Zhingyi Furgão	",
                "Effa Hafei Zhingyi Picape	",
                "Effa Hafei Zhingyi Van	",
                "Hafei (Towner) Towner Furgão	",
                "Hafei (Towner) Towner Passageiro	",
                "Hafei (Towner) Towner Pick0Up	",
                "Jaguar XJ5	",
                "Jaguar XK Series	",
                "Kia Soul Sportage	",
                "Mahindra Mahindra Chassi	",
                "Mahindra Mahindra Pick0up	",
                "Mahindra Mahindra SUV	",
                "Maserati Coupé	",
                "Mercedes Calsse SLK	",
                "Mercedes Classe E Cabrio	",
                "Mercedes Classe E Coupé	",
                "Mercedes Classe E Touring	",
                "Mercedes Classe SL	",
                "Mitsubishi L200 Triton RS	",
                "Porsche 911 Cabrio	",
                "Renault Clio Htach	",
                "Volkswagen Kombi Furgão	",
                "Volkswagen Passar Variant");

    List<String> ano = Arrays.asList (
            "2010-2010",
            "2010-2011",
            "2011-2011",
            "2011-2012",
            "2012-2012",
            "2012-2013",
            "2013-2013",
            "2013-2014",
            "2014-2014",
            "2014-2015",
            "2015-2015",
            "2015-2016",
            "2016-2016",
            "2016-2017",
            "2017-2017",
            "2017-2018");

}