
set JION=
if "%2%" == "" goto executeTask
set JION=-joinpoint %2
:executeTask
if "%1%" == "all" goto executeAllTask
java -jar japp_deployment_cmd-1.0.jar -component %1 -instruction deploy %JION%
goto end

:executeAllTask
set moduleNames=japp_access_back japp_append_task japp_business_controller japp_business_controller104 japp_cancel_order japp_lottery_issue japp_partner japp_partner_account japp_partner_order japp_risk_control japp_ticket_issue japp_ticket_winning japp_user_account japp_user_order jweb_access
for /D %%i in (%moduleNames%) do java -jar japp_deployment_cmd-1.0.jar -component %%i -instruction deploy %JION%
:end