'use strict'

var module = angular.module('demo.controllers', []);
module.controller("UserController", [ "$scope", "UserService",
		function($scope, UserService) {

			$scope.userDto = {
				userId : null,
				userName : null,
				skillDtos : []
			};
			$scope.fieldDto = {
				domain : null,
				session : null,
				field : null
			};
			$scope.ftypeOptions = {
				"Text" : "Text",
				"Number" : "Number",
				"Date" : "Date",
				"Email" : "Email",
				"Checkbox" : "Checkbox",
				"Currency" : "Currency",
				"Picklist" : "Picklist"
			};
			$scope.sepratorList = {
				"," : "Comma",
				";" : "Colan",
				"\n" : "Newline"
			};
			$scope.domains = {
				"prod" : "Production",
				"sandbox" : "Sandbox",
				"custom" : "Custom"
			};
			// For test only
			$scope.jsonData = [
			    {id: "1", type: "bus", make: "VW", color: "white"},
			    {id: "2", type: "taxi", make: "BMW", color: "blue"}
			  ];
			  
			$scope.description = "";
			$scope.helptext = "";
			$scope.domain = "";
			$scope.mydomain = "";
			$scope.seprator = ",";
			$scope.ftype = "Text";
			$scope.flen = "";
			$scope.flen1 = "";
			$scope.flen2 = "0";
			$scope.skills = [];
			$scope.hasSession = false;
			$scope.fields = "";
			$scope.picklistValues = "";
			$scope.selectedSobject = "";
			$scope.sobjectdata = {};
			$scope.defaultvalue = "";
			
			
			UserService.getAccessCode().then(function(value) {
				$scope.accessCode = value.data;
				// If we have session then we do further processing
				if($scope.accessCode.length > 0) {
					// Fetch the list of sObjects
					var sfoutput = $scope.accessCode[0].code;
					$scope.session= JSON.parse(sfoutput);
					console.log("session > ",$scope.session);
					$scope.hasSession = true;
					sessionscreen(true);
					$("#loadingSobjets").show();
					UserService.getsObjects($scope.session.access_token, $scope.session.instance_url).then(function(value) {
						var sobjectData = value.data;
						var sobjects = sobjectData.sobjects; 
						console.log("sobjectData  > ",sobjects);
						for(var r in sobjects) {
							if(sobjects[r].layoutable == true) {
								$scope.sobjectdata[sobjects[r].name] = sobjects[r].label;
							}
						}
						$("#loadingSobjets").hide();
					}, function(reason) {
						console.log("error occured");
						$("#loadingSobjets").hide();
					}, function(value) {
						console.log("no callback");
						$("#loadingSobjets").hide();
					});
					
				}
				
			}, function(reason) {
				console.log("error occured");
			}, function(value) {
				console.log("no callback");
			});
			
			/****
			UserService.getUserById(1).then(function(value) {
				console.log(value.data);
			}, function(reason) {
				console.log("error occured");
			}, function(value) {
				console.log("no callback");
			});
			*****/
			
			$scope.clearSession = function() {
				UserService.deletesession().then(function(value) {
					$scope.session = {};
					$scope.hasSession = false;
					sessionscreen(false);
					$("#fTable").empty();
					//row2
				}, function(reason) {
					console.log("error occured");
				}, function(value) {
					console.log("no callback");
				});
			}

			$scope.saveUser = function() {
				$scope.userDto.skillDtos = $scope.skills.map(skill => {
					return {skillId: null, skillName: skill};
				});
				UserService.saveUser($scope.userDto).then(function() {
					console.log("works");
					UserService.getAllUsers().then(function(value) {
						$scope.allUsers= value.data;
					}, function(reason) {
						console.log("error occured");
					}, function(value) {
						console.log("no callback");
					});

					$scope.skills = [];
					$scope.userDto = {
						userId : null,
						userName : null,
						skillDtos : []
					};
				}, function(reason) {
					console.log("error occured");
				}, function(value) {
					console.log("no callback");
				});
			}
			
			$scope.toggleLenFields = function(){
				console.log($scope.flen);
				if($scope.ftype == "Number" || $scope.ftype == "Currency") {
					$("#flen").hide();
					$("#flen1").show();
					$("#picklist").hide();
					$("#flendiv").show();
					$("#flen2").show();
				} else if($scope.ftype == "Picklist") {
					$("#picklist").show();
					$("#flendiv").hide();
				} else if($scope.ftype == "Checkbox") {
					$("#flendiv").hide();
				} else {
					$("#flendiv").show();
					$("#flen").show();
					$("#flen1").hide();
					$("#picklist").hide();
					$("#flen2").hide();
				}
			}
			$scope.refreshPage = function() {
				$scope.description = "";
				$scope.helptext = "";
				$scope.domain = "";
				$scope.mydomain = "";
				$scope.seprator = ",";
				$scope.ftype = "Text";
				$scope.flen = "";
				$scope.flen1 = "";
				$scope.flen2 = "0";
				$scope.skills = [];
				$scope.hasSession = false;
				$scope.fields = "";
				$scope.selectedSobject = "";
				//$scope.sobjectdata = {};
				$scope.defaultvalue = "";
				$("#flen").hide();
				$("#flen1").show();
				$("#flen2").show();
				$("#fTable").empty();
				sessionscreen(true);
			}
			
			$scope.buyMeACoffee = function() {
				window.open("https://www.buymeacoffee.com/happychef", '_blank').focus();
			}
			
			$scope.createField = function() {
				
				$("#creatingFields").show();
				$("#fTable").empty();
				$("#fTable").append("<tr><th>#</th><th>Field Name</th><th>Status</th><th>Status Code</th></tr>");
				var errorMsg = "";
				if($scope.fields == "" ) {
					errorMsg += "*Please Enter Field names. \n";
				}
				
				if($scope.selectedSobject == "" ) {
					errorMsg += "*Please Select sObject. \n";
				}
				
				if($scope.ftype == "" ) {
					errorMsg += "*Please Select Field Type. \n";
				}
				if($scope.ftype == "Text") {
					if($scope.flen == "" || isNaN($scope.flen)) {
						errorMsg += "*Please Select valid Field Length. \n";
					}
				} else if($scope.ftype == "Checkbox") {
					if($scope.picklistValues == "") {
						errorMsg += "*Please enter Picklist values. \n";
					}
				} else if($scope.ftype == "Number" || $scope.ftype ==  "Currency") {
					if($scope.flen1 == "" || isNaN($scope.flen2)) {
						errorMsg += "*Please Select valid Field Length. \n";
					}
					
					else if((Number($scope.flen1) + Number($scope.flen2)) > 18) {
						errorMsg += "*The sum of the length and decimal places must be an integer less than or equal to 18 \n";
					}
				}
				
				
				
				if(errorMsg != "") {
					$("#creatingFields").hide();
					alert(errorMsg);
					return;
				}
				$(".row6").show();
				console.log($scope.fields);
				console.log($scope.seprator);
				console.log($scope.fields.split($scope.seprator));
				const uniqueValues = new Set($scope.fields.split($scope.seprator));

				
				Promise.all(Array.from(uniqueValues).map(function(value) {
				    console.log(2,value);
				    return createField(value);
				})).then(function(results) {
				    console.log(1,results);
				    createLead(uniqueValues.size);
				});

			}
			
			function createLead(count) {
				
				var leadDto = {
					"count" : count,
					"sessionId" : $scope.session.access_token,
					"domain" : $scope.session.instance_url,
					"id" :  $scope.session.id
 				};
				console.log(4,leadDto);
				UserService.createLead(leadDto).then(function(value) {
					console.log(5,value);
				}, function(reason) {
					console.log(reason);
				}, function(value) {
					console.log(value);
				});
			}
			
			$scope.loginSF = function() {
				
				if($scope.domain == "") {
					alert('Please provide a Login Type');
					return;
				}
				
				if($scope.domain == "custom") {
					if($scope.mydomain == "" || !validURL($scope.mydomain)) {
						alert('Please provide a valid Domain');
						return;
					}
				}
				window.location.href = "/oauth2/sflogin?env="+$scope.domain+"&customdomain="+$scope.mydomain;
			}
			
			function validURL(str) {
			  var pattern = new RegExp('^(https?:\\/\\/)?'+ // protocol
			    '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|'+ // domain name
			    '((\\d{1,3}\\.){3}\\d{1,3}))'+ // OR ip (v4) address
			    '(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*'+ // port and path
			    '(\\?[;&a-z\\d%_.~+=-]*)?'+ // query string
			    '(\\#[-a-z\\d_]*)?$','i'); // fragment locator
			  return !!pattern.test(str);
			}
			
			function createField(fname) {
				$("#creatingFields").show();
				var fieldObject = {};
				fieldObject["FullName"] =  $scope.selectedSobject+"."+fname+"__c";
				var Metadata = {};
				Metadata["label"] = fname;
				Metadata["description"] = $scope.description;
				Metadata["inlineHelpText"] = $scope.helptext;
				Metadata["required"] = false;
				Metadata["externalId"] = false;
				Metadata["type"] = $scope.ftype;
				
				
				
				if($scope.ftype == "Number" || $scope.ftype == "Currency") {
					Metadata["precision"] = $scope.flen1;
	    			Metadata["scale"] = $scope.flen2;
				} else if ($scope.ftype == "Text") {
					Metadata["length"] = $scope.flen;
				}	
				
				if($scope.ftype == "Checkbox") {
					Metadata["defaultValue"] = false; 
				} else {
					Metadata["defaultValue"] = $scope.defaultvalue;
				}
				
				if($scope.ftype == "Picklist") {
					const uniquePickValues = new Set($scope.picklistValues.split($scope.seprator));
					console.log(uniquePickValues);
					// format : 
					//var pick = '{"valueSetDefinition":{"value":[{"label":"Canada","valueName":"Canada"},{"label":"US","valueName":"US"}]}}';
					var valueSetDefinition = {};
					var valueSet = {};
					var value = [];
					for(var pick of uniquePickValues) {
						value.push({"label" : pick , "valueName" : pick});
					}
					valueSetDefinition["value"] = value;
					valueSet["valueSetDefinition"] = valueSetDefinition;
					Metadata["valueSet"] = valueSet; 
					console.log(valueSet);
				}
				
							
				fieldObject["Metadata"] =  Metadata;
				console.log(Metadata);
				var jsonField = JSON.stringify(fieldObject);
				//$scope.fieldDto.domain = $scope.session.instance_url;
				//$scope.fieldDto.session = $scope.session.access_token;
				//$scope.fieldDto.field = jsonField;
				var fieldDto = {
					domain : $scope.session.instance_url,
					session : $scope.session.access_token,
					field : jsonField
				};
				UserService.createField(fieldDto).then(function(value) {
					console.log(3,value);
					if(Array.isArray(value.data)) {
						$("#fTable").append("<tr><td>"+value.data.id+"</td><td>"+fname+"</td><td>"+value.data[0].message+"</td><td>"+value.data[0].errorCode.replace(/_/g, ' ')+"</td></tr>");
					} else {
						$("#fTable").append("<tr><td>"+value.data.id+"</td><td>"+fname+"</td><td>Created</td><td>200[OK]</td></tr>");
					}
					$("#creatingFields").hide();
					return value;
				}, function(reason) {
					$("#fTable > tbody").append("<tr><td></td><td>"+fname+"</td><td>Not Created</td><td>ERROR:Unknown</td></tr>");
					console.log("error occured",reason);
					$("#creatingFields").hide();
					return reason;
				}, function(value) {
					console.log("no callback",value);
					$scope.hasSession = false;
					$("#creatingFields").hide();
					return value;
				});
			}
			
			
		} ]);
		
		
$( document ).ready(function() {
	console.log('ready!');
	$("div.row1:visible").parents('div.data').addClass("login");
    $("#orgtype").change(function() {
    	console.log('onchange!' , $("#orgtype :selected").text());
    	if($("#orgtype :selected").text() == 'Custom' ) {
    		$(".row2").show();
    	} else {
    		$(".row2").hide();
    	}
    });
});

function createFieldsJS() {

	

}

function sessionscreen(isSession){
	if(isSession) {
		$(".row4").show();
		$(".row5").show();
		$(".row1").hide();
		$(".row2").hide();
		$(".row3").hide();
		$(".loginscreenimage").hide();
		$(".workscreenimage").show();
		$("div.row4:visible").parents('div.data').removeClass("login");
	} else {
		$(".row4").hide();
		$(".row5").hide();
		$(".row1").show();
		$(".row2").hide();
		$(".row3").show();
		$("div.row1:visible").parents('div.data').addClass("login");
		$(".loginscreenimage").show();
		$(".workscreenimage").hide();
	}
}	
		