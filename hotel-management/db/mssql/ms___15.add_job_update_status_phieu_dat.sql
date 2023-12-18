USE msdb;

BEGIN TRY
    BEGIN TRANSACTION MyTransaction;

    IF NOT EXISTS (SELECT 1 FROM dbo.sysjobs WHERE name = 'UpdateStatusReservation')
        BEGIN
            -- Step 1: Create a new job
            DECLARE @jobId UNIQUEIDENTIFIER;
            EXEC msdb.dbo.sp_add_job
                 @job_name = 'UpdateStatusReservation',
                 @enabled = 1,
                 @description = 'This job is created with the purpose to update reservation status every day at 5 AM';
            SELECT @jobId = job_id
            FROM dbo.sysjobs
            WHERE name = 'UpdateStatusReservation';

            -- Step 2: Create a job step
            EXEC msdb.dbo.sp_add_jobstep
                 @job_id = @jobId,
                 @step_id = 1,
                 @step_name = 'Execute SP_UPDATE_STATUS_RESERVATION',
                 @command = 'EXEC [dbo].[SP_UPDATE_STATUS_RESERVATION]',
                 @on_success_action = 1, -- 1 for quit with success
                 @on_fail_action = 2; -- 2 for quit with failure

            -- Step 3: Schedule the job
            EXEC msdb.dbo.sp_add_schedule
                 @schedule_name = 'DailyAt5AM',
                 @freq_type = 4, -- Daily
                 @freq_interval = 1, -- Every 1 day
                 @active_start_time = 50000, -- 5:00 AM (HHMMSS format)
                 @active_end_time = 235959; -- All day

            -- Step 4: Attach the schedule to the job
            EXEC msdb.dbo.sp_attach_schedule
                 @job_id = @jobId,
                 @schedule_name = 'DailyAt5AM';

            -- Step 5: Enable the job
            EXEC msdb.dbo.sp_update_job
                 @job_id = @jobId,
                 @enabled = 1;

            -- Step 6: Associate the job with a job server
            EXEC msdb.dbo.sp_add_jobserver
                 @job_name = 'UpdateStatusReservation',
                 @server_name = @@SERVERNAME;

            -- Step 7: Start the job
            EXEC msdb.dbo.sp_start_job
                 @job_name = 'UpdateStatusReservation';
            COMMIT TRANSACTION MyTransaction;
        END
    ELSE
        BEGIN
            RAISERROR
                (N'Đã tồn tại jobs trong database!', 16, 1);
        END
END TRY
BEGIN CATCH
    PRINT 'An error occurred: ' + ERROR_MESSAGE();
    ROLLBACK TRANSACTION MyTransaction;
    DECLARE
        @ErrorMsg NVARCHAR(4000);
    SET
        @ErrorMsg = N'Lỗi: ' + ERROR_MESSAGE();
    THROW
        51000, @ErrorMsg, 1;
END CATCH;

/*
-- Delete job
USE msdb;
EXEC msdb.dbo.sp_delete_job @job_name = 'UpdateStatusReservation';


-- Select all jobs
USE msdb;
SELECT job_id, name, enabled
FROM dbo.sysjobs;


-- See job command
USE msdb;
SELECT j.name AS JobName, js.step_id, js.step_name, js.command
FROM dbo.sysjobs j
         JOIN dbo.sysjobsteps js ON j.job_id = js.job_id
ORDER BY j.name, js.step_id;
 */