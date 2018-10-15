#!/bin/bash
APP_BASE=/opt/cis
APP=cis-admissions
APP_VERSION=BETA12


APP_TYPE=war
APP_PATH=$APP_BASE/$APP

LOGFILE=$APP_PATH/$APP.log
PIDFILE=$APP_PATH/$APP.pid


PROFILE=prod-rc


RETVAL=0


start() {
        echo -n $"Starting $APP"
        touch $LOGFILE
        cd $APP_PATH
        java -DPIDFILE=$PIDFILE -Dspring.profiles.active=$PROFILE -jar $APP_PATH/$APP-$APP_VERSION.$APP_TYPE > $LOGFILE &
        echo
}

stop() {
	
        if [ -e $PIDFILE ]
        then
                PID=`cat $PIDFILE`
                echo -n $"Stopping $APP:"
                kill $PID
                echo
        else
                echo -n $"Not running"
        fi
}

check_status() {
        if [ -e $PIDFILE ]; then
                PID=`cat $PIDFILE`
                if ps -p $PID > /dev/null; then
                        return 1
                fi
        fi
        return 0
}

case "$1" in
        start)
                start
                ;;
        stop)
                stop
                ;;
        restart)
                stop
                start
                ;;
        status)
                if ! check_status; then
                        echo "$APP: Running"
                else
                        echo "$APP: Not Running"
                fi
                ;;
        *)
                echo $"Usage: $0 {start|stop|restart|status}"
                RETVAL=1
esac
exit $RETVAL

                                
