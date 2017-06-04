<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width" />
    <title>Share</title>
    <script src="https://cdn.bootcss.com/angular.js/1.6.3/angular.min.js"></script>
</head>

<body>
    <div ng-app="share" ng-controller="getShare">
        <#list list as item>
            <h1>标题: ${item.title}</h1>
            <h3>内容: ${item.content}</h3>
            <#--<p>预拨打电话: ${item.phoneNumberLianXi}</p>
            <p>与发送短信的号码: ${item.messagePhoneNumber}</p>
            <p>预发送短信: ${item.messageContent}</p>-->
            <p>提醒时间: ${item.years}.${item.months}.${item.days} ${item.hours}:${item.minutes}</p>
            <p>图片</p>
        <#list item.address as addr>
            <img ng-src="{{changeStr('${addr.address}')}}" width="200px" height="200px">
        </#list>
        </#list>

    </div>
    <script>
        var app = angular.module('share', []);
        app.controller('getShare', function($scope, $http) {
            $scope.changeStr = function(param) {
                var param = param.substring(6);
                return '/pics/' + param;
            };
        });
    </script>
</body>
</html>