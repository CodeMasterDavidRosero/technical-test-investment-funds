Param(
    [string] $Profile = "cloud",
    [switch] $SkipTests
)

$ErrorActionPreference = "Stop"

$repoRoot = Join-Path $PSScriptRoot ".."
$envFile = Join-Path $repoRoot ".env"

if (-not (Test-Path $envFile)) {
    Write-Error ".env not found at $envFile"
    exit 1
}

# Load key=value pairs from .env (ignores empty lines and comments)
Get-Content $envFile | Where-Object { $_ -notmatch '^\s*(#|$)' } | ForEach-Object {
    $kv = $_ -split '=', 2
    if ($kv.Length -eq 2) {
        [Environment]::SetEnvironmentVariable($kv[0], $kv[1])
    }
}

$repoLocal = Join-Path $repoRoot ".m2\repository"
if (-not (Test-Path $repoLocal)) {
    New-Item -ItemType Directory -Path $repoLocal | Out-Null
}

$mvnw = Join-Path $repoRoot "mvnw.cmd"
$args = @("-Dmaven.repo.local=$repoLocal", "-Dspring-boot.run.profiles=$Profile", "spring-boot:run")
if ($SkipTests) {
    $args = @("-Dmaven.repo.local=$repoLocal", "-DskipTests", "-Dspring-boot.run.profiles=$Profile", "spring-boot:run")
}

& $mvnw @args
